class ToDoListApplication {
    public static void main(String[] args) {
        System.out.println("ToDoList Application v0.2\n");
        ToDoListController controller = new ToDoListController();
        controller.mainLoop();
    }
}
