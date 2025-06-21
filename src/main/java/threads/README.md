# Thread Utilities

This package contains demonstrations of the most common Java concurrency constructs. Each class is heavily commented so you can understand what every method does.

## Basic thread creation

* **ThreadExample** – minimal example of extending `Thread`.
* **RunnableExample** – implements `Runnable` and runs the task in a `Thread`.
* **ThreadMethodsDemo** – showcases `Thread` methods such as `setPriority`, `start`, `join`, `isAlive` and `interrupt`.

## Returning results

* **CallableExample** – uses `Callable` with `FutureTask` to return a value.
* **FutureDemo** – explores operations on `Future` including `get`, `cancel` and running a `FutureTask` directly.

## Executor frameworks

* **ExecutorServiceDemo** – illustrates fixed, cached and single-thread pools, bulk operations like `invokeAll`/`invokeAny` and graceful shutdown. It also creates a scheduled executor for periodic tasks.
* **ScheduledExecutorExample** – focuses exclusively on `ScheduledExecutorService`, showing delayed and periodic execution and how to cancel the returned `ScheduledFuture`.

Additional classes demonstrate locks and other synchronization primitives such as semaphores and stamped locks.

## Fork/Join framework

* **forkjoin/ForkJoinDemo** – demonstrates using `ForkJoinPool` along with `RecursiveAction` and `RecursiveTask`.
* **forkjoin/SumRecursiveAction** – sums a range of an array without returning a value.
* **forkjoin/FibonacciRecursiveTask** – calculates a Fibonacci number and returns the result.

Run any example using `java` or from your IDE.
