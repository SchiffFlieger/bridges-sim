package de.karstenkoehler.bridges;

import de.karstenkoehler.bridges.io.ParseResult;
import de.karstenkoehler.bridges.io.ValidateException;
import de.karstenkoehler.bridges.io.parser.ParseException;
import de.karstenkoehler.bridges.io.parser.Parser;
import de.karstenkoehler.bridges.io.parser.TokenConsumingParser;
import de.karstenkoehler.bridges.io.parser.token.TokenizerImpl;
import de.karstenkoehler.bridges.io.validators.DefaultValidator;
import de.karstenkoehler.bridges.io.validators.Validator;
import de.karstenkoehler.bridges.ui.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main_Koehler_Karsten extends Application {
    public static void main(String[] args) {
        mainConsole();
//        mainGui(args);
        System.out.println("finish");
    }

    public static void mainGui(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = createRootPane(stage);
        Scene scene = new Scene(root);

        stage.setTitle("Bridges Simulator - Karsten Köhler - 8690570");
        stage.setScene(scene);
        stage.show();
    }

    private Parent createRootPane(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/main.fxml"));
        Parent root = loader.load();
        MainController controller = loader.getController();
        controller.setStage(stage);
        return root;
    }

    public static void mainConsole() {
        Validator validator = new DefaultValidator();

        File dir = new File("src\\main\\resources\\data\\");
        for (File file : dir.listFiles()) {
            if (!file.isFile()) {
                continue;
            }

            try {
                Parser parser = new TokenConsumingParser(new TokenizerImpl(readFile(file.getAbsolutePath())));
                ParseResult result = parser.parse();
                validator.validate(result);
                System.out.printf("%-30s %2dx%2d %3d islands, %3d bridges\n", file.getName(), result.getWidth(), result.getHeight(), result.getIslands().size(), result.getBridges().size());

            } catch (IOException | ParseException | ValidateException e) {
                e.printStackTrace();
            }

        }
    }

    private static String readFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)), Charset.defaultCharset());
    }


}
