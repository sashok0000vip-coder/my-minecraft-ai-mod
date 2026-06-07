package com.yourname.aimod;

// Импортируем компоненты чата Minecraft
import net.minecraft.network.chat.Component;
// Импортируем класс игрока на сервере
import net.minecraft.server.level.ServerPlayer;
// Импортируем систему событий Forge
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// Импортируем инструменты Java для работы с сетью и текстом
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

// Аннотация @Mod говорит игре, что этот класс — главный в моде.
// ID мода ("aimod") должен строго совпадать с тем, что указан в mods.toml
@Mod("aimod")
public class AiMod {

    // Сюда нужно вставить твой личный секретный ключ от Google AI Studio
    private static final String API_KEY = "AQ.Ab8RN6KtBIqO8Y7ggC8wVq3BbhjCRzraIZ065Ul_XY7g5RPcUg"; 
    
    // Адрес (URL), по которому наш мод будет отправлять запросы к нейросети Gemini
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;

    // Конструктор мода. Он срабатывает один раз при запуске игры
    public AiMod() {
        // Мы регистрируем этот класс в Forge, чтобы игра знала: мы хотим отслеживать события (например, сообщения в чате)
        MinecraftForge.EVENT_BUS.register(this);
    }// Аннотация @SubscribeEvent говорит Forge, что метод ниже управляет игровым событием
    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        // Получаем чистый текст, который написал игрок
        String messageText = event.getRawText();
        // Получаем объект самого игрока, который это написал
        ServerPlayer player = event.getPlayer();

        // Проверяем: если сообщение НАЧИНАЕТСЯ со слова "Житель,"
        if (messageText.startsWith("Житель Diesel,")) { 
            // Отсекаем слово "Житель," чтобы оставить только сам вопрос к ИИ
            String prompt = messageText.replace("Житель,", "").trim();

            // КРИТИЧЕСКИ ВАЖНО: Мы создаем новый поток (Thread) для запроса в интернет.
            // Если этого не сделать, Майнкрафт полностью завит ("намертво") на 1-2 секунды, 
            // пока ждет ответа от серверов Google. Новый поток позволяет игре работать плавно.
            new Thread(() -> {
                // Вызываем функцию askGemini (описана ниже) и ждем от нее текстовый ответ
                String aiResponse = askGemini(prompt);
                
                // Отправляем полученный ответ обратно в чат игроку.
                // Знак "§6" делает имя [Житель] золотым, а "§f" делает текст ответа белым.
                player.sendSystemMessage(Component.literal("§6[Житель]: §f" + aiResponse));
            }).start(); // Запускаем фоновый поток
        }
    }// Аннотация @SubscribeEvent говорит Forge, что метод ниже управляет игровым событием
    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        // Получаем чистый текст, который написал игрок
        String messageText = event.getRawText();
        // Получаем объект самого игрока, который это написал
        ServerPlayer player = event.getPlayer();

        // Проверяем: если сообщение НАЧИНАЕТСЯ со слова "Житель,"
        if (messageText.startsWith("Житель Diesel,")) { 
            // Отсекаем слово "Житель," чтобы оставить только сам вопрос к ИИ
            String prompt = messageText.replace("Житель,", "").trim();

            // КРИТИЧЕСКИ ВАЖНО: Мы создаем новый поток (Thread) для запроса в интернет.
            // Если этого не сделать, Майнкрафт полностью завит ("намертво") на 1-2 секунды, 
            // пока ждет ответа от серверов Google. Новый поток позволяет игре работать плавно.
            new Thread(() -> {
                // Вызываем функцию askGemini (описана ниже) и ждем от нее текстовый ответ
                String aiResponse = askGemini(prompt);
                
                // Отправляем полученный ответ обратно в чат игроку.
                // Знак "§6" делает имя [Житель] золотым, а "§f" делает текст ответа белым.
                player.sendSystemMessage(Component.literal("§6[Житель]: §f" + aiResponse));
            }).start(); // Запускаем фоновый поток
        }
    }
