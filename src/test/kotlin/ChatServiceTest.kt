import org.junit.Test

import org.junit.Assert.*
class ChatServiceTest {

    val userOne = User(1, "Иван", mutableMapOf())
    val userTwo = User(2, "Петя", mutableMapOf())
    val userTree = User(3, "Макс", mutableMapOf())
    val service = ChatService

    fun nU(){
        service.chatStorage.clear()
        service.idChat = 0
        service.idMessage = 0
    }

    @Test
    fun createChat() {
        nU()
        service.createChat("Чат между пользователями ${userTree.userName} и ${userOne.userName}",userTree, userOne )
        assertFalse(service.chatStorage.isEmpty())
    }

    //Если пусто
    @Test
    fun sendMessage_chatStorage_isEmpty() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        assertFalse(service.chatStorage.isEmpty())
    }


    //если уже есть сообщения
    @Test
    fun sendMessage_chatStorage_not_isEmpty() {
        nU()
        service.sendMessage(userTwo, userTree, "Проверка1")
        service.sendMessage(userTwo, userTree, "Проверка2")
        assertTrue(service.chatStorage.size == 1)

    }

    //если уже есть чаты но между этими пользователями создан впервые
    @Test
    fun sendMessage_chatStorage_not_isEmpty_New_Chat() {
        nU()
        service.sendMessage(userTwo, userTree, "Проверка2")
        service.sendMessage(userTree, userOne, "Проверка3")
        assertTrue("Чат между пользователями ${userTree.userName} и ${userOne.userName}" == service.chatStorage[1]!!.titles)
    }

    @Test
    fun createMessage() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        assertTrue(service.chatStorage[0]!!.chatUsers[0].text == "Проверка")
    }

    //Сообщение прочитано
    @Test
    fun readingMessage() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        val chatId = 0
        val messageId = 0
        assertFalse(service.chatStorage[chatId]?.chatUsers?.first{ messageId == it.messageId }?.readabilityId!!)
    }

    //Сообщение было прочитано ранее
    @Test
    fun readingMessage_Earlier() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        val chatId = 0
        val messageId = 0
        service.readingMessage(chatId, messageId)
        assertTrue(service.chatStorage[chatId]?.chatUsers?.first{ messageId == it.messageId }?.readabilityId!!)
    }

    //Сообщения не существует
    @Test
    fun readingMessage_Null() {
        service.chatStorage.clear()
        service.sendMessage(userOne, userTwo, "Проверка")
        service.sendMessage(userOne, userTwo, "Проверка")
        val chatId = 0
        val messageId = 0
        service.readingMessage(chatId, messageId)
        assertNull(service.chatStorage[chatId]?.chatUsers?.first{ messageId == it.messageId }?.readabilityId)
    }


    //Чат удален
    @Test
    fun deleteChat() {
        nU()
        service.idMessage = 0
        service.sendMessage(userOne, userTwo, "Проверка")
        service.deleteChat(userOne, userTwo)
        assertTrue(service.chatStorage.isEmpty())
    }

    //Чат удален
    @Test
    fun deleteChat_No_Users() {
        nU()
        service.deleteChat(userOne, userTwo)
        assertTrue(service.chatStorage.isEmpty())
    }

    //Чат удален
    @Test
    fun deleteChat_No_Users_isEmpty() {
        nU()
        service.idMessage = 0
        service.sendMessage(userOne, userTwo, "Проверка")
        service.deleteChat(userOne, userTree)
        assertTrue(service.chatStorage.size == 1)
    }

    //Вывести все чаты пользователя
    @Test
    fun outputChats() {
        nU()
        service.idMessage = 0
        service.sendMessage(userOne, userTwo, "Проверка")
        service.sendMessage(userOne, userTree, "Проверка")
        service.outputChats(userOne)
        assertTrue(service.chatStorage.size == 2)
    }

    //Вывести все чаты пользователя Exception
    @Test(expected = ChatNotFoundException::class)
    fun outputChats_Exception() {
        nU()
        service.outputChats(userOne)
    }

    @Test
    fun getListOfMessages() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        assertFalse(service.getListOfMessages(userOne,0).isEmpty())
    }

    //Отредактировать сообщения пользователя
    @Test
    fun editMessageInChat() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        service.sendMessage(userOne, userTwo, "Проверка12")
        service.editMessageInChat(userOne, "Gooooo", 0)
        assertTrue(service.chatStorage[0]!!.chatUsers.first { it.messageId == 1 }.text == "Gooooo")
    }

    //Не смогли отредактировать
    @Test(expected = ChatNotFoundException::class)
    fun editMessageInChat_No() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        service.sendMessage(userOne, userTwo, "Проверка12")
        service.editMessageInChat(userOne, "Gooooo", 7)
    }

    //удаление чата
    @Test
    fun deleteMessage() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        assertTrue(service.deleteMessage(userOne, 0))
    }

    //Такого чата не существует
    @Test
    fun deleteMessage_No() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        assertFalse(service.deleteMessage(userOne, 1))
    }

    //Такого чата не существует Null
    @Test
    fun deleteMessage_Null() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        assertFalse(service.deleteMessage(userTree, 1))
    }

    @Test
    fun getUnreadChatsCount() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        service.sendMessage(userOne, userTwo, "Проверка12")
        assertTrue(service.getUnreadChatsCount() == 1)
    }

    @Test
    fun getListOfChatsWithMessages() {
        nU()
        service.sendMessage(userOne, userTwo, "Проверка")
        service.sendMessage(userOne, userTwo, "Проверка12")
        val chat = service.getListOfChatsWithMessages(userOne)
        assertNotNull(chat)
    }
}