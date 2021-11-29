
class ChatNotFoundException(message : String) : RuntimeException(message)
fun main() {
    val service = ChatService
    val userOne = User(1, "Иван", mutableMapOf())
    val userTwo = User(2, "Петя", mutableMapOf())
    val userTree = User(3, "Макс", mutableMapOf())


//    Петя пишет ивану
    service.sendMessage(userTwo, userOne, "Привет! Как дела")
    service.sendMessage(userOne, userTwo, "Привет! Все норм")
//    service.deleteChat(userTree, userTwo)
    service.sendMessage(userTree, userTwo,"Привет Макс!")
    service.sendMessage(userTree, userOne,"Здарова Макс!")

    service.sendMessage(userOne, userTree,"Привет, как жизнь?!")
    service.sendMessage(userTwo, userTree,"Привет Петя! Как дела?")
    service.sendMessage(userTree, userTwo,"Зашибись все Макс")
    service.sendMessage(userTree, userOne,"Неплохо, меня кстати зовот Иван")
    service.readingMessage(0,0)
    service.readingMessage(1,0)
    service.readingMessage(2,1)
    service.readingMessage(6,1)
    service.readingMessage(3,2)
//
////    service.readingMessage()
//
//
//
//    println(service.chatStorage.values) // вывод всех чатов в памяти
//    println()
//    println("Все чаты $userOne "+ service.outputChats(userOne)) //Вывод всех чатов Ивана
//    println()
//    println("Вывод списка сообщений чата в котором участвует Петя")
//    println(service.getListOfMessages(userTwo, 1))
////
//    service.editMessageInChat(userTwo, "Не, все плохо, Макс", 0)
//    println("Все чаты $userTwo " + service.outputChats(userTwo)) //Вывод всех чатов Ивана
//    println(service.getListOfMessages(userTwo, 0))
////    println("11111 чат")
////    println(service.chatStorage[0]!!.chatUsers) // вывод всех чатов в памяти
////    println("22222 чат")
////    println(service.chatStorage[1]!!.chatUsers) // вывод всех чатов в памяти
////    println("33333 чат")
////    println(service.chatStorage[2]!!.chatUsers) // вывод всех чатов в памяти
//
//    service.deleteChat(userTree, userOne)
//    println("Количество чатов с непрочитанными сообщениями: " + service.getUnreadChatsCount())
//
//    println(service.getListOfChatsWithMessages(userTwo))
//
//    service.deleteMessage(userOne, 1)
}