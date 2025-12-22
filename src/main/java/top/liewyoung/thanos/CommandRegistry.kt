package top.liewyoung.thanos

data class Command(val name: String, val obj: Any)

class CommandRegistry {
    /*命令注册表*/
    private val commands = mutableListOf<Command>();
    private var engine: Python3Engine? = null;

    constructor(engine: Python3Engine?){
        this.engine = engine
    }

    /**
     * 全部注册
     * @param [command] 指令实体
     */
    fun registerAll(vararg command: Command) {
        for (c in command) {
            commands.add(c)
            engine?.inject(c.name, c.obj)
        }
    }

    fun register(command: Command) {
        commands.add(command)
        engine?.inject(command.name, command.obj)
    }

    fun changeEngine(engine: Python3Engine){
        this.engine = engine
        for (c in commands) {
            engine.inject(c.name, c.obj)
        }
    }
}