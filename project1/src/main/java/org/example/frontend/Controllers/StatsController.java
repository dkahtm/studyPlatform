package org.example.frontend.Controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import org.example.frontend.Services.StatService;
import org.json.JSONObject;

public class StatsController {

    @FXML
    private PieChart pieChart;

    private final StatService statsService = new StatService();

    private int groupId;

    // Викликається з HomeController
    public void setGroupId(int groupId) {
        System.out.println("🔥 setGroupId CALLED with groupId = " + groupId);
        this.groupId = groupId;
        loadStats();
    }

    private void loadStats() {
        new Thread(() -> {
            try {
                JSONObject json = statsService.getStatsByGroup(groupId);

                long open = json.getLong("OPEN");
                long inProgress = json.getLong("IN_PROGRESS");
                long done = json.getLong("DONE");

                ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                        new PieChart.Data("OPEN", open),
                        new PieChart.Data("IN_PROGRESS", inProgress),
                        new PieChart.Data("DONE", done)
                );

                Platform.runLater(() -> {
                    pieChart.setData(pieChartData);
                    pieChart.setTitle("Статистика задач групи #" + groupId);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}


