import controller.TaskController;

class ToDoListApplication {
    public static void main(String[] args) {
        System.out.println("ToDoList Application v1.0\n");
        TaskController controller = new TaskController();
        controller.mainLoop();
    }
}
