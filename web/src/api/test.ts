export interface TestItem {
  id: number
  name: string
  description: string
  done: boolean
  createdAt: string
}

export interface TestItemPayload {
  name: string
  description: string
  done: boolean
}

interface ApiError {
  detail?: string
  message?: string
  error?: string
}

const BASE_URL = 'http://localhost:10086/api/test/items'

async function request<T>(url: string, init?: RequestInit): Promise<T> {
  const res = await fetch(url, init)
  if (!res.ok) {
    let message = `HTTP ${res.status} ${res.statusText}`
    const body = (await res.json().catch(() => null)) as ApiError | null
    if (body) {
      message = body.detail ?? body.message ?? body.error ?? message
    }
    throw new Error(message)
  }
  if (res.status === 204) {
    return undefined as T
  }
  return (await res.json()) as T
}

function jsonInit(method: 'POST' | 'PUT', payload: TestItemPayload): RequestInit {
  return {
    method,
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  }
}

export const testItemApi = {
  list: () => request<TestItem[]>(BASE_URL),
  get: (id: number) => request<TestItem>(`${BASE_URL}/${id}`),
  create: (payload: TestItemPayload) => request<TestItem>(BASE_URL, jsonInit('POST', payload)),
  update: (id: number, payload: TestItemPayload) =>
    request<TestItem>(`${BASE_URL}/${id}`, jsonInit('PUT', payload)),
  remove: (id: number) => request<void>(`${BASE_URL}/${id}`, { method: 'DELETE' }),
}
