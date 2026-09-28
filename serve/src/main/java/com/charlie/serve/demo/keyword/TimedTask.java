package com.charlie.serve.demo.keyword;

/**
 * 【学习演示】Task 的子类：演示继承体系的五个关键字
 * extends / super / @Override / protected（覆盖钩子）/ private（继承壁垒）。
 */
public class TimedTask extends Task {

	/** private final：子类自己的字段，照旧 private + final。 */
	private final int estimateMinutes;

	/**
	 * 子类构造器：第一行必须通过 super(...) 完成父类初始化。
	 * 不写时编译器会自动插入无参 super()，但父类没有无参构造器，所以这里必须显式传参。
	 */
	public TimedTask(String title, int estimateMinutes) {
		super(title); // 复用父类的校验逻辑
		this.estimateMinutes = estimateMinutes;
	}

	public int getEstimateMinutes() {
		return estimateMinutes;
	}

	/**
	 * @Override protected：重写父类钩子，补上"预估耗时"。
	 * @Override 是"编译期校验器"：方法名/参数写错时立刻报错，
	 *           而不是悄悄变成一个新方法（≈ TS 里也建议开启的 override 检查）。
	 */
	@Override
	protected String describeExtra() {
		return " (预估 " + estimateMinutes + " 分钟)";
	}

	@Override
	public String toString() {
		return "TimedTask{id=" + getId() + ", title='" + getTitle() + "', estimate=" + estimateMinutes + "min}";
	}

	// ============ 以下两处取消注释都会"编译报错"——这正是这些关键字的防呆价值 ============

	// ① final 方法不可重写：summary() 的骨架由父类定死，子类只能通过钩子填空。
	// @Override
	// public String summary() {
	// return "想篡改骨架？编译错误。";
	// }

	// ② private 字段对子类不可见：id 由父类全权管理，子类碰不到。
	// void breakId() {
	// this.id = 999; // ❌ 编译错误：id has private access in Task
	// }
}
