package top.liewyoung.thanos


class EasterEgg{
    private val thinking = "To be or not to be, which is a question."
    private val todo = String.format("1.以后用Compose重写。\n2.我也不知道谁会发现这个。")

    fun getThinking(): String{
        return thinking
    }

    fun getTodo(): String{
        return todo
    }
}