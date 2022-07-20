package controllers;

public class AutoSavePageController {
    private static AutoSavePageController autoSavePageController = null;

    private AutoSavePageController(){

    }

    public static AutoSavePageController getAutoSavePageController(){
        if(autoSavePageController == null)
            autoSavePageController = new AutoSavePageController();
        return autoSavePageController;
    }

    public boolean checkNumberOfSavedFilesErrors(String input){
        if(!input.matches("[0-9]+") || input.length() > 3)
            return true;
        int number = Integer.parseInt(input);
        if(!(number > 0 && number < 11))
            return true;
        return false;
    }
}
