package com.charlie.serve.demo.keyword;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 【学习演示】入口类：main / static / final 局部变量 / var / lambda / record。
 *
 * <p>
 * 运行方式：IDE 里直接 Run 本文件的 main 方法；
 * 或命令行：{@code java -cp target/classes com.charlie.serve.demo.KeywordDemo}
 * </p>
 */
public class KeywordDemo {

	/**
	 * 入口方法签名四要素：
	 * {@code public} —— JVM 需要从外部调用它；
	 * {@code static} —— JVM 在"还没有任何对象"时就要调用它，所以不能是实例方法；
	 * {@code void} —— 不向 JVM 返回结果；
	 * {@code main(String[] args)} —— 名字与参数形状是 JVM 的硬约定。
	 */
	public static void main(String[] args) {
		printSection("1. final 局部变量 ≈ 前端 const");
		final Task first = new Task("写关键字 Demo");
		// first = new Task("再写一个"); // ❌ 编译错误：final 局部变量只能赋值一次
		System.out.println("first  = " + first);
		printSection("2. var：局部类型推断（仍是强类型，只是省去重复书写）");
		var second = new TimedTask("前端类比学习", 30);
		System.out.println("second = " + second);

		printSection("3. 模板方法：final 骨架 + protected 钩子");
		System.out.println("second.summary() = " + second.summary());
		System.out.println("first.summary()  = " + first.summary());

		printSection("4. 接口回调：Comparable 让 JDK 排序工具认识 Task");
		List<Task> tasks = new ArrayList<>();
		tasks.add(second);
		tasks.add(first);
		tasks.add(new Task("买菜"));
		Collections.sort(tasks); // 触发 compareTo：你写的代码被 JDK 回调
		tasks.forEach(t -> System.out.println("  " + t.summary())); // lambda ≈ JS 箭头函数

		printSection("5. static 成员：属于类，不属于任何实例");
		System.out.println("创建总数 = " + Task.getCreatedCount());
		System.out.println("标题上限 = " + Task.MAX_TITLE_LENGTH);

		printSection("6. record：语法糖版不可变类");
		var snapshot = new Snapshot(first.getId(), first.summary(), first.isDone());
		System.out.println("snapshot = " + snapshot);
		// record 的隐含待遇：类是 final（不能继承）、字段全部 private final、
		// 自动生成构造器 / 访问器 / equals / hashCode / toString。
	}

	/** private static：只服务本类、无需实例的辅助方法。 */
	private static void printSection(String title) {
		System.out.println();
		System.out.println("== " + title + " ==");
	}

	/**
	 * 嵌套 record：不可变数据快照。
	 * 对比项目里的 TestItemVO —— 同一个模式的正式版。
	 */
	record Snapshot(long id, String summary, boolean done) {
	}
}
