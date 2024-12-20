package com.example;

import java.io.IOException;
import javafx.fxml.FXML;

public class gameController {

    @FXML
    private void newGame() throws IOException {
        System.out.println("New Game");
        App.loadFXML("C:\\Users\\PC\\Desktop\\Tech\\VGU\\3rd_year\\Fra-Uas\\OOP_Java\\aop\\src\\main\\resources\\com\\example\\secondary");
    }

    public void closeProgram() {
        Utils.closeProgram();
    }
}

