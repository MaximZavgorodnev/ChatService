import org.junit.Test

import org.junit.Assert.*
import java.lang.RuntimeException
import kotlin.reflect.KClass

class ChatServiceTest {

    val userOne = User(1, "Иван", mutableMapOf())
    val userTwo = User(2, "Петя", mutableMapOf())
    val userTree = User(3, "Макс", mutableMapOf())
    val service = ChatService

    @Test
    fun createChat() {
        service.chatStorage.clear()
        service.idChat = 0
        service.idMessage = 0
        service.createChat("Чат между пользователями ${userTree.userName} и ${userOne.userName}",userTree, userOne )
        assertFalse(service.chatStorage.isEmpty())
    }

    //Если пусто
    @Test
    fun sendMessage_chatStorage_isEmpty() {
        service.chatStorage.clear()
        service.idChat = 0
        service.idMessage = 0
        service.sendMessage(userOne, userTwo, "Проверка")
        assertFalse(service.chatStorage.isEmpty())
    }


    //если уже есть сообщения
    @Test
    fun sendMessage_chatStorage_not_isEmpty() {
        service.chatStorage.clear()
        service.idChat = 0
        service.idMessage = 0
        service.sendMessage(userTwo, userTree, "Проверка1")
        service.sendMessage(userTwo, userTree, "Проверка2")
        assertTrue(service.chatStorage.size == 1)

    }

    //если уже есть чаты но между этими пользователями создан впервые
    @Test
    fun sendMessage_chatStorage_not_isEmpty_New_Chat() {
        service.chatStorage.clear()
        service.idChat = 0
        service.idMessage = 0
        service.sendMessage(userTwo, userTree, "Проверка2")
        service.sendMessage(userTree, userOne, "Проверка3")
        assertTrue("Чат между пользователями ${userTree.userName} и ${userOne.userName}" == service.chatStorage[1]!!.titles)
    }

    @Test
    fun createMessage() {
        service.chatStorage.clear()
        service.idChat = 0
        service.idMessage = 0
        service.sendMessage(userOne, userTwo, "Проверка")
        assertTrue(service.chatStorage[0]!!.chatUsers[0].text == "Проверка")
    }

    //Сообщение прочитано
    @Test
    fun readingMessage() {
        service.chatStorage.clear()
        service.idChat = 0
        service.idMessage = 0
        service.sendMessage(userOne, userTwo, "Проверка")
        val chatId = 0
        val messageId = 0
        assertFalse(service.chatStorage[chatId]?.chatUsers?.first{ messageId == it.messageId }?.readabilityId!!)
    }

    //Сообщение было прочитано ранее
    @Test
    fun readingMessage_Earlier() {
        service.chatStorage.clear()
        service.idChat = 0
        service.idMessage = 0
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
        service.chatStorage.clear()
        service.idChat = 0
        service.idMessage = 0
        service.sendMessage(userOne, userTwo, "Проверка")
        service.deleteChat(userOne, userTwo)
        assertTrue(service.chatStorage.isEmpty())
    }

    //Чат удален
    @Test
    fun deleteChat_No_Users() {
        service.chatStorage.clear()
        service.idChat = 0
        service.idMessage = 0
        service.deleteChat(userOne, userTwo)
        assertTrue(service.chatStorage.isEmpty())
    }

    //Чат удален
    @Test
    fun deleteChat_No_Users_isEmpty() {
        service.chatStorage.clear()
        service.idChat = 0
        service.idMessage = 0
        service.sendMessage(userOne, userTwo, "Проверка")
        service.deleteChat(userOne, userTree)
        assertTrue(service.chatStorage.size == 1)
    }


}