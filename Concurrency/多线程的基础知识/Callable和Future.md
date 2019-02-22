# Callable
Callable接口是类似于Runnable的接口，实现Callable接口的类和实现Runnable接口的类都是可被其他线程执行的任务。
1. Callable规定的方法是call()，而Runnable规定的方法是run().
2. Callable的任务执行后可返回值，而Runnable的任务是不能返回值的。  
3. call()方法可抛出异常，而run()方法是不能抛出异常的。
4. 运行Callable任务可拿到一个Future对象， Future表示异步计算的结果。它提供了检查计算是否完成的方法，以等待计算的完成，并检索计算的结果。通过Future对象可了解任务执行情况，可取消任务的执行，还可获取任务执行的结果。


# Future
Future表示异步计算的结果，它提供了检查计算是否完成的方法，以等待计算的完成，并检索计算的结果，通过Future对象可了解任务执行情况，可取消任务的执行，还可获取任务的执行结果。


# 使用
两者配合使用使用，调用Callable 类
