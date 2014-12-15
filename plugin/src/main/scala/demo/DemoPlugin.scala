package demo

import scala.tools.nsc.io.VirtualFile
import scala.tools.nsc.util.BatchSourceFile
import scala.tools.nsc.{ Global, Phase }
import scala.tools.nsc.plugins.{ Plugin, PluginComponent }

class DemoPlugin(val global: Global) extends Plugin {
  import global._

  override def init(options: List[String],  error: String => Unit): Boolean = true

  val name = "demo"
  val description = "a plugin"
  val components = List[PluginComponent](DemoComponent)

  private object DemoComponent extends PluginComponent {

    val global = DemoPlugin.this.global
    import global._

    override val runsAfter = List("parser")
    override val runsBefore = List("namer")

    val phaseName = "Demo"

    override def newPhase(prev: Phase) = new GlobalPhase(prev) {
      override def run() = {
        val code = "object Hello"
        val virtualFile = new VirtualFile(name) {
          override def file = new java.io.File(".")
        }
        val sourceFile = new BatchSourceFile(virtualFile, code)
        val unit = new CompilationUnit(sourceFile)
        unit.body = global.newUnitParser(code).parse()
        global.currentRun.compileLate(unit)
      }

      def name: String = phaseName

      def apply(unit: global.CompilationUnit): Unit = {}
    }
  }
}
