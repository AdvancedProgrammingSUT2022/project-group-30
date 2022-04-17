package views;

import controllers.MainPageController;

public class MainPageView implements View{
    private static MainPageView mainPageView;

    private MainPageController controller;

    private MainPageView(){
        controller = null;
    }

    public static MainPageView getMainPageView(){
        return mainPageView == null ? new MainPageView() : mainPageView;
    }

    public void run(){

    }

    public void showMenu(){

    }

    public void setController(){
        this.controller = MainPageController.getMainPageController();
        this.controller.setProgramDatabase();
    }

    public MainPageController getController(){
        return this.controller;
    }
    
}
