<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { testItemApi, type TestItem } from '@/api/test'

const items = ref<TestItem[]>([])
const loading = ref(false)
const errorMessage = ref('')
const editingId = ref<number | null>(null)

const form = reactive({
  name: '',
  description: '',
  done: false,
})

async function run(action: () => Promise<void>) {
  loading.value = true
  errorMessage.value = ''
  try {
    await action()
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : String(error)
  } finally {
    loading.value = false
  }
}

async function load() {
  await run(async () => {
    items.value = await testItemApi.list()
  })
}

function resetForm() {
  editingId.value = null
  form.name = ''
  form.description = ''
  form.done = false
}

async function handleSubmit() {
  await run(async () => {
    const payload = { name: form.name, description: form.description, done: form.done }
    if (editingId.value === null) {
      await testItemApi.create(payload)
    } else {
      await testItemApi.update(editingId.value, payload)
    }
    resetForm()
    items.value = await testItemApi.list()
  })
}

function startEdit(item: TestItem) {
  editingId.value = item.id
  form.name = item.name
  form.description = item.description
  form.done = item.done
}

async function toggleDone(item: TestItem) {
  await run(async () => {
    await testItemApi.update(item.id, {
      name: item.name,
      description: item.description,
      done: !item.done,
    })
    items.value = await testItemApi.list()
  })
}

async function removeItem(item: TestItem) {
  await run(async () => {
    await testItemApi.remove(item.id)
    if (editingId.value === item.id) {
      resetForm()
    }
    items.value = await testItemApi.list()
  })
}

function formatTime(value: string) {
  return new Date(value).toLocaleString()
}

onMounted(load)
</script>

<template>
  <main class="test-view">
    <h1>Test CRUD 演示</h1>
    <p class="hint">Vue 页面 → HTTP → Spring Boot Controller → Service → JPA → H2 数据库</p>

    <form class="form" @submit.prevent="handleSubmit">
      <input v-model="form.name" placeholder="name（必填，最长 50）" maxlength="50" />
      <input v-model="form.description" placeholder="description（最长 200）" maxlength="200" />
      <label class="checkbox">
        <input v-model="form.done" type="checkbox" />
        done
      </label>
      <button type="submit" :disabled="loading || form.name.trim() === ''">
        {{ editingId === null ? '新增' : '保存修改' }}
      </button>
      <button v-if="editingId !== null" type="button" class="ghost" @click="resetForm">
        取消编辑
      </button>
    </form>

    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>

    <div class="toolbar">
      <span>共 {{ items.length }} 条</span>
      <button type="button" :disabled="loading" @click="load">刷新</button>
    </div>

    <table>
      <thead>
        <tr>
          <th>id</th>
          <th>name</th>
          <th>description</th>
          <th>done</th>
          <th>createdAt</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="loading && items.length === 0">
          <td colspan="6" class="empty">加载中...</td>
        </tr>
        <tr v-else-if="items.length === 0">
          <td colspan="6" class="empty">暂无数据，先新增一条吧</td>
        </tr>
        <template v-else>
          <tr v-for="item in items" :key="item.id" :class="{ editing: item.id === editingId }">
            <td>{{ item.id }}</td>
            <td>{{ item.name }}</td>
            <td>{{ item.description }}</td>
            <td>
              <input
                type="checkbox"
                :checked="item.done"
                :disabled="loading"
                @change="toggleDone(item)"
              />
            </td>
            <td>{{ formatTime(item.createdAt) }}</td>
            <td class="actions">
              <button type="button" @click="startEdit(item)">编辑</button>
              <button type="button" class="danger" @click="removeItem(item)">删除</button>
            </td>
          </tr>
        </template>
      </tbody>
    </table>
  </main>
</template>

<style scoped>
.test-view {
  max-width: 960px;
  margin: 0 auto;
  padding: 1rem;
}

h1 {
  font-size: 1.4rem;
}

.hint {
  color: var(--color-text);
  opacity: 0.7;
  font-size: 0.85rem;
}

.form {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  align-items: center;
  margin: 1rem 0;
}

.form input[type='text'],
.form input:not([type]) {
  padding: 0.4rem 0.6rem;
  border: 1px solid var(--color-border);
  border-radius: 4px;
}

.form input:first-child {
  flex: 1 1 180px;
}

.form input:nth-child(2) {
  flex: 2 1 240px;
}

.checkbox {
  display: inline-flex;
  gap: 0.3rem;
  align-items: center;
  font-size: 0.9rem;
}

button {
  padding: 0.4rem 0.9rem;
  border: 1px solid var(--color-border);
  border-radius: 4px;
  background: transparent;
  cursor: pointer;
}

button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

button.danger {
  color: #d33;
  border-color: #d33;
}

button.ghost {
  border-style: dashed;
}

.error {
  color: #d33;
  font-size: 0.9rem;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
}

table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.9rem;
}

th,
td {
  border: 1px solid var(--color-border);
  padding: 0.45rem 0.6rem;
  text-align: left;
}

th {
  background: rgba(127, 127, 127, 0.1);
}

tr.editing {
  background: rgba(127, 127, 127, 0.08);
}

td.empty {
  text-align: center;
  opacity: 0.6;
}

.actions {
  display: flex;
  gap: 0.4rem;
}
</style>
