package saneforce.sanclm.applicationCommonFiles;

public interface AsyncTaskCompleteListener<T>{


    public void onTaskComplete(T result,int label);
}
