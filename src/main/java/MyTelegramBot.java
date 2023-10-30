import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;

public class MyTelegramBot extends TelegramLongPollingBot {
    final String BOT_NAME;
    final String BOT_TOKEN;
    final String URL = "https://api.nasa.gov/planetary/apod" +
            "?api_key=ieTZ9ahI07X6ePw1yXXAs0rATkaQZxrJPiqlAMpw";
    private static final String HELP_TEXT = "Демонстрация моего бота:\n\n" +
            "Вы можете выполнять команды из главного меню слева или набрав команду:\n\n" +
            "Введите /start, чтобы увидеть приветственное сообщение\n\n" +
            "Введите /help, чтобы снова увидеть это сообщение\n\n" +
            "Введите /give, чтобы загрузить изображение Nasa\n\n" +
            "Введите /date, чтобы загрузить изображение Nasa на определенную дату";

    public MyTelegramBot(String BOT_NAME, String BOT_TOKEN) {
        this.BOT_NAME = BOT_NAME;
        this.BOT_TOKEN = BOT_TOKEN;

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String answer = update.getMessage().getText();

            long chatId = update.getMessage().getChatId();

            switch (answer) {
                case "Старт":
                case "/start":
                    sendMessage("Привет! Я бот, который присылает картинку от NASA", chatId);
                    break;
                case "Помощь":
                case "/help":
                    sendMessage(HELP_TEXT, chatId);
                    break;
                case "Картинка дня":
                case "/give":
                    String url = Utils.getUrl(URL);
                    sendMessage(url, chatId);
                    break;
                case "/date":
                case "Введите дату":
                    sendMessage("Введите дату в формате YYYY-MM-DD:", chatId);
                    break;
                case "/explanation":
                case "Описание картинки":
                    String urlExplanation = Utils.getExplanation(URL);
                    sendMessage(urlExplanation, chatId);
                    break;
                default:
                    if (answer.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        String date = answer;
                        sendMessage(Utils.getUrl(URL + "&date=" + date), chatId);
                    } else
                        sendMessage("Моя твоя не понимай", chatId);
            }
        }
    }

    void sendMessage(String msg, long chatId) {
        SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add("Старт");
        keyboardFirstRow.add("Помощь");

        keyboard.add(keyboardFirstRow);

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add("Картинка дня");
        keyboardSecondRow.add("Описание картинки");

        keyboard.add(keyboardSecondRow);

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add("Введите дату");

        keyboard.add(keyboardThirdRow);

        replyKeyboardMarkup.setKeyboard(keyboard);

        message.setChatId(chatId);
        message.setText(msg);
        message.setReplyMarkup(replyKeyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Не смог отослать сообщение");
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
