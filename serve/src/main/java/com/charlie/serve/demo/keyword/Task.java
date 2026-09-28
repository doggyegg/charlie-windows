package com.charlie.serve.demo.keyword;

import java.time.Instant;
import java.util.Objects;

/**
 * 【学习演示】关键字全覆盖的任务类，不参与任何业务，可安全删除。
 *
 * <p>
 * 看点：public/private/protected 的可见性分层、final 的三种落点、static 的类级共享、
 * "闸门"式校验、final 骨架 + protected 钩子的模板方法模式。
 * </p>
 *
 * <p>
 * 本类刻意不加 final：允许被继承（见 TimedTask）。若想禁止继承可加 final，
 * 但在 Spring 项目里要谨慎——final 类无法被 CGLIB 代理，AOP 会失败。
 * </p>
 */
public class Task implements Comparable<Task> {

	/** public static final：常量。类级共享、永不改变、对外公开的规则值（≈ 前端 export const）。 */
	public static final int MAX_TITLE_LENGTH = 50;

	/** private static：类级共享的计数器，所有实例共用同一份（≈ JS 模块顶层的变量）。仅本类可见。 */
	private static long createdCount = 0;

	/** private final：仅本类可见 + 出生后锁死。赋值窗口只有构造器，之后谁都不能再改。 */
	private final long id;

	/** private：可变状态。外部只能通过 setTitle() 这个"闸门"修改，校验拦在门口。 */
	private String title;

	/** private：默认 false。状态只能通过 complete() 走合法迁移。 */
	private boolean done;

	/** private final：创建时间定稿，不可再变。 */
	private final Instant createdAt;

	// 反例（取消注释即编译报错）：构造器之外的任何位置再赋值 final 字段都会被拒绝。
	// void reId() { this.id = 100; } // ❌ cannot assign a value to final variable
	// 'id'

	/**
	 * public 构造器：外部创建 Task 的唯一入口，Spring 场景下也是"构造器注入"的调用点。
	 *
	 * @param title 任务标题（会做非空/长度校验）
	 */
	public Task(String title) {
		this.id = ++createdCount; // static 计数器自增；this 明确指向"当前这个实例"
		this.title = requireValidTitle(title); // 调用 private static 工具方法做校验
		this.createdAt = Instant.now();
	}

	// ---------- 只读访问：字段私有，getter 公开 ----------

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public boolean isDone() {
		return done;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	/** public static 方法：不需要创建实例即可调用（≈ 模块顶层导出的函数）。 */
	public static long getCreatedCount() {
		return createdCount;
	}

	// ---------- 唯一合法的修改通道 ----------

	/** public：对外的承诺操作。内部校验细节不暴露，将来改校验规则不影响调用方。 */
	public void setTitle(String title) {
		this.title = requireValidTitle(title);
	}

	/** 状态机规则收在类内部：已完成的任务不能重复完成。 */
	public void complete() {
		if (done) {
			throw new IllegalStateException("任务 #" + id + " 已经完成，不能重复完成");
		}
		this.done = true;
	}

	// ---------- 模板方法：final 骨架 + protected 钩子 ----------

	/**
	 * final 方法：子类禁止重写。骨架（状态标记 + id + 标题 + 扩展段）由父类定死，
	 * 但预留 describeExtra() 让子类"填空"——这就是模板方法模式。
	 */
	public final String summary() {
		String status = done ? "[√]" : "[ ]";
		return status + " #" + id + " " + title + describeExtra();
	}

	/**
	 * protected 钩子：仅"同包 + 子类"可见，不进入对外公开 API。
	 * 默认返回空串，子类可按需覆盖（见 TimedTask）。
	 */
	protected String describeExtra() {
		return "";
	}

	// ---------- 接口契约 ----------

	/**
	 * implements Comparable&lt;Task&gt;：把"怎么比较两个 Task"交给本类实现。
	 * 排序时 Collections.sort / List.sort 会回调此方法——Java 接口在运行时真实存在，
	 * 是框架与业务代码之间的契约（区别于 TS interface 的编译期擦除）。
	 */
	@Override
	public int compareTo(Task other) {
		// private 是"类私有"而非"对象私有"：同一个类的不同实例之间也能互相访问字段。
		return Long.compare(this.id, other.id);
	}

	/** 覆盖 Object.toString，便于打印观察。 */
	@Override
	public String toString() {
		return "Task{id=" + id + ", title='" + title + "', done=" + done + "}";
	}

	// ---------- 内部工具 ----------

	/** private static：纯校验函数，不依赖实例（static），也不希望外泄（private）。 */
	private static String requireValidTitle(String raw) {
		String trimmed = Objects.requireNonNull(raw, "title 不能为 null").trim();
		if (trimmed.isEmpty()) {
			throw new IllegalArgumentException("title 不能为空白");
		}
		if (trimmed.length() > MAX_TITLE_LENGTH) {
			throw new IllegalArgumentException("title 最长 " + MAX_TITLE_LENGTH + " 个字符");
		}
		return trimmed;
	}
}
