import java.time.LocalDateTime
import kotlin.io.println as println

object ChatService {
    var chatStorage: MutableMap<Int, Chat> = mutableMapOf()
    var idChat: Int = 0
    var idMessage: Int = 0
    var timeMessageCall: LocalDateTime = LocalDateTime.now()


    //Создаем чат
//    fun createChat(titles: String, //Название чата
//                   userTwo: User,//Пользователь кому пишется сообщение
//                   userOne: User,//Пользователь который пишет сообщение
//    ) : Chat { val newChat = Chat(chatId = idChat, chatUsers = mutableListOf(),titles = titles, unreadMessages = 0)
//        chatStorage.put(newChat.chatId,newChat)
//        idChat++
//        userTwo.memoryOfMyChats[newChat.chatId] = userOne.userId
//        userOne.memoryOfMyChats[newChat.chatId] = userTwo.userId
//        return newChat
//    }

    //Отправить сообщение пользователю(создать чат, сообщение и отправить его)
    fun sendMessage(userTwo: User,//Пользователь кому пишется сообщение
                    userOne: User,//Пользователь который пишет сообщение
                    text: String) //Текст сообщения
    {
       //Написание сообщения по новому
        try {
            val chatId = userTwo.memoryOfMyChats.filter { userTwo.memoryOfMyChats.containsKey(userOne.userId) }.getValue(userOne.userId)

            var message = Message(
                messageId = idMessage , //Идентификатор сообщения
                chatId = chatId, //Идентификатор чата
                messageRecipientId = userTwo.userId, //Получатель сообщения
                messageSenderId = userOne.userId, //Отправитель сообщения
                userNameSender = userOne.userName,//Имя отправителя сообщения
                text = text, //Текст сообщения
                dateTime = timeMessageCall, // Дата и время создания сообщения в формате Unixtime
                readabilityId = false )
            idMessage++
            chatStorage.getValue(chatId).chatUsers.add(message)
            chatStorage.getValue(chatId).unreadMessages++
            println("Сообщение отправлено")
        } catch (e: NoSuchElementException){
//            println("Чата между ними нет")
            println("Создан новый чат между пользователями ${userTwo.userName} и ${userOne.userName}")
            val newChat = Chat(chatId = idChat, chatUsers = mutableListOf(),titles = "Чат между пользователями ${userTwo.userName} и ${userOne.userName}", unreadMessages = 0)
            chatStorage.put(newChat.chatId, newChat)
            idChat++
            userTwo.memoryOfMyChats[userOne.userId] = newChat.chatId
            userOne.memoryOfMyChats[userTwo.userId] = newChat.chatId
        }



        //Проверка на присутствие чата между пользователями

//
//        var chatId = chatStorage.filter { chatStorage.isNullOrEmpty() }.keys.first { it == userTwo.memoryOfMyChats.filterValues { it == userOne.userId}.keys.elementAt(0) }
//        createMessage(userTwo,userOne, text, chatId)
//
//        chatId = chatStorage.filter { userTwo.memoryOfMyChats.containsValue(userOne.userId) }.keys.first { it == userTwo.memoryOfMyChats.filterValues { it == userOne.userId}.keys.elementAt(0) }
//        val chatId1 = chatStorage.filter { !userTwo.memoryOfMyChats.containsValue(userOne.userId) }.values.reduce { acc, chat -> val ch = createChat() }
//
//
//        when{
//            !chatStorage.isEmpty() -> {
//                when (userTwo.memoryOfMyChats.containsValue(userOne.userId)) {
//                    true -> {
//                        val number = userTwo.memoryOfMyChats.filterValues { it == userOne.userId}.keys
//                        val chatId = chatStorage.keys.first { it == number.elementAt(0) }
//                        createMessage(userTwo,userOne, text, chatId)
//                    }
//                    else -> {
//                        println("Создан новый чат между пользователями ${userTwo.userName} и ${userOne.userName}")
//                        val chat = createChat(
//                            "Чат между пользователями ${userTwo.userName} и ${userOne.userName}",
//                            userTwo,//Айди второго участника чата
//                            userOne) //Айди первого участника чата))
//                        createMessage(userTwo,userOne, text, chat.chatId)
//                    }
//                }
//            }
//            else -> {
//                println("Создан новый чат между пользователями ${userTwo.userName} и ${userOne.userName}")
//                    val chat = createChat("Чат между пользователями ${userTwo.userName} и ${userOne.userName}",
//                    userTwo,//Айди кому написали
//                    userOne)//Айди кто написал
//                createMessage(userTwo,userOne, text, chat.chatId)
//            }
//        }
    }

//    //Создание сообщения
//    fun createMessage(userTwo: User,//Пользователь кому пишется сообщение
//                      userOne: User,//Пользователь который пишет сообщение
//                      text: String, //Текст сообщения
//                      ) {
//
//
//    }

    //Прочтение сообщения
    fun readingMessage(messageId: Int, chatId: Int){
        try {
            chatStorage.filter { !chatStorage.getValue(chatId).chatUsers.first { messageId == it.messageId }
                .readabilityId }.getValue(chatId).chatUsers.first { messageId == it.messageId }.readabilityId = true
            println("Cообщение прочитано")
        } catch (e: NoSuchElementException){
            println("Cообщение не существует")
        }


//        when(chatStorage[chatId]?.chatUsers?.first{ messageId == it.messageId }?.readabilityId){
//            false -> {
//                chatStorage[chatId]!!.chatUsers.first{ messageId == it.messageId }.readabilityId = true //Прочли сообщение
//                println("Сообщение прочитано")
//                chatStorage[chatId]!!.unreadMessages--
//            }
//            true -> println("Сообщение уже было прочитано")
//            null -> println("Сообщения не существует")
//        }
    }

    //Удалить чаты пользователя
    fun deleteChat(userTwo: User,//Пользователь чат с которым хотят удалить
                   userOne: User,//Пользователь который хочет удалить чат
    ) {
        try {
            chatStorage.remove(userOne.memoryOfMyChats[userTwo.userId])
        } catch (e: NoSuchElementException){
            println("Чата между пользователями не существует")
        }


//        when {
//            !chatStorage.isEmpty() -> {
//                when (userTwo.memoryOfMyChats.containsValue(userOne.userId)) {
//                    true -> {
//                        val number = userTwo.memoryOfMyChats.filterValues { it == userOne.userId }.keys
//                        val chatId = chatStorage.keys.first { it == number.elementAt(0) }
//                        chatStorage.remove(chatId)
//                        println("Чат между пользователями ${userTwo.userName} и ${userOne.userName} удален")
//                    }
//                    else -> { println("Чата между пользователями ${userTwo.userName} и ${userOne.userName} не существует") }
//                }
//            }
//            else -> { println("Чата между пользователями ${userTwo.userName} и ${userOne.userName} не существует") }
//        }
    }

    //Вывести все чаты пользователя
    fun outputChats(userOne: User): Collection<Chat>{
        return  chatStorage.filter{ !userOne.memoryOfMyChats.isEmpty() }.filter{ userOne.memoryOfMyChats.values.contains(it.component1()) }.values
//        when {!chatStorage.isEmpty() -> {
//            val number = userOne.memoryOfMyChats.keys
//            val chats = chatStorage.filter { number.contains(it.component1()) }.values
//            return chats
//            }
//            else -> {throw ChatNotFoundException("У пользователя ${userOne.userName} нет чатов")}
//        }
    }

    //Получить список сообщений из чата
    fun getListOfMessages(userOne: User, idChat: Int): MutableList<Message> {
        val idChats = idChat
        //            (0..idChat).shuffled().last()
        try {
            val chat = outputChats(userOne).first {it.chatId == idChats}.chatUsers
            return chat
        } catch (e: NoSuchElementException) {
            throw ChatNotFoundException("У пользователя ${userOne.userName} нет чатов")
        }
    }

    //Отредактировать сообщение пользователя в этом чате
    fun editMessageInChat(userOne: User, text: String, idChats: Int){
//            (0..idChat).shuffled().last()
        val idMessages = 1
//            (0..idMessage).shuffled().last()
//        val chat = getListOfMessages(userOne, idChats)
////        println(chat)
        try {
            chatStorage.getValue(idChats).chatUsers.first { it.messageId == idMessages }.text = text
        } catch (e: NoSuchElementException){
            println("У пользователя ${userOne.userName} нет чатов")
        }
//        when (chatStorage[idChats] != null){
//            true -> chatStorage[idChats]!!.chatUsers.first { it.messageId == idMessages }.text = text
//            else -> throw ChatNotFoundException("У пользователя ${userOne.userName} таких сообщений нет")
//        }
    }


    //Удалить сообщения пользователя в этом чате(при удалении последнего сообщения в чате весь чат удаляется)
    fun deleteMessage(userOne: User, chatId: Int) {
        try {
            chatStorage.getValue(chatId).chatUsers.removeAll{ message -> message.messageRecipientId == userOne.userId || message.messageSenderId == userOne.userId }
            println("Сообщения удалены")
        } catch (e: NoSuchElementException) {
            println("Такого чата не существует")
        }



//       when(chatStorage[chatId]?.chatUsers?.removeAll { message -> message.messageRecipientId == userOne.userId || message.messageSenderId == userOne.userId }) {
//           true -> {println("Сообщение удалено")
//               return true}
//           false -> {println("Такого чата не существует")
//           return false}
//           null -> {println("Такого чата не существует")
//               return false}
//       }
    }

    //Получить информацию о количестве непрочитанных чатов (например, service.getUnreadChatsCount) -
    // это количество чатов, в каждом из которых есть хотя бы одно непрочитанное сообщение
    fun getUnreadChatsCount(): Int {
        return chatStorage.values.filter { chat -> chat.unreadMessages !=0 }.size
    }

    //Получить список чатов пользователя - где в каждом чате есть последнее сообщение (если нет, то пишется "нет сообщений")
    fun getListOfChatsWithMessages(userOne: User): Collection<Chat> {
        return chatStorage.filter { userOne.memoryOfMyChats.keys.contains(it.component1()) }.values
    }
}