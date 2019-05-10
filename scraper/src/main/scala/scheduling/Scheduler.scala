package scheduling

import akka.actor.{Actor, Cancellable, Props}
import api.{ApiClient, Request}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object Scheduler {
  def props: Props = Props(new Scheduler)
}

class Scheduler extends Actor {
  private val apiClient = context.actorOf(ApiClient.props, "apiClient")
  implicit val ec = ExecutionContext.global

  private val tasks: Map[String, Cancellable] = Map()

  override def receive: Receive = onMessage(tasks)

  private def addTask(task: RecurringTask): Unit = {
    if (tasks.contains(task.name)) {
      tasks(task.name).cancel()
    }
    val cancellable = context.system.scheduler.schedule(0 milliseconds, task.interval, apiClient, task.request)
    context.become(onMessage(tasks + (task.name -> cancellable)))
  }

  private def removeTask(task: StopTask): Unit = {
    if (tasks.contains(task.name)) {
      tasks(task.name).cancel()
    }
    context.become(onMessage(tasks - task.name))
  }

  private def onMessage(tasks: Map[String, Cancellable]): Receive = {
    case task: RecurringTask =>
      addTask(task)
      println(s"\n === Recurring Task registered: $task === \n")
    case task: Task =>
      apiClient ! task.request
      println(s"\n === One time task requested: $task === \n")
    case task: StopTask =>
      removeTask(task)
      println(s"\n === Recurring Task unregistered: $task === \n")
  }
}

case class Task(request: Request[_])

case class RecurringTask(name: String, request: Request[_], interval: scala.concurrent.duration.FiniteDuration)

case class StopTask(name: String)

