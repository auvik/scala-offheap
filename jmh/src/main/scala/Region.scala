package offheap.test.jmh

import org.openjdk.jmh.annotations._
import offheap._

@State(Scope.Thread)
class RegionClose {
  var r: Region = _

  //@Param(Array("1", "2", "4", "8", "16", "32", "64", "128", "256", "512", "1024"))
  @Param(Array("1024", "2048", "4096"))
  var allocatedPages: Int = _

  @Setup(Level.Invocation)
  def setup(): Unit = {
    r = Region.open
    for (_ <- 1 to allocatedPages)
      r.allocate64(internal.Setting.pageSize)
  }

  @Benchmark
  def close = r.close()
}

@State(Scope.Thread)
class RegionOpen {
  var r: Region = _

  @TearDown(Level.Invocation)
  def tearDown(): Unit = r.close

  @Benchmark
  def open = {
    r = Region.open
    r
  }
}

@State(Scope.Thread)
class RegionAllocateCurrent {
  var r: Region = _

  @Setup(Level.Invocation)
  def setup(): Unit =
    r = Region.open

  @TearDown(Level.Invocation)
  def tearDown(): Unit = r.close()

  @Benchmark
  def allocate = r.allocate64(16L)
}

@State(Scope.Thread)
class RegionAllocateNext {
  var r: Region = _

  @Setup(Level.Invocation)
  def setup(): Unit = {
    r = Region.open
    r.allocate64(internal.Setting.pageSize)
  }

  @TearDown(Level.Invocation)
  def tearDown(): Unit = r.close()

  @Benchmark
  def allocate = r.allocate64(16L)
}



