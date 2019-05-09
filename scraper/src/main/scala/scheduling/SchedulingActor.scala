package scheduling

import akka.actor.{Actor, Props}

object SchedulingActor {
  def props: Props = Props(new SchedulingActor)
}

class SchedulingActor extends Actor {

  def start(): Unit = {
    println("Scheduler has started!")
  }

  override def receive: Receive = {
    case "start" => start()
    case _ => println("Unknown command")
  }
}
