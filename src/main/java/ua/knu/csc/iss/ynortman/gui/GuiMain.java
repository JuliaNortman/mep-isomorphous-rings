package ua.knu.csc.iss.ynortman.gui;

public class GuiMain {
    public static void main(String[] args) {
        String mode = args[0];
        int k = 25;
        if(args.length > 1) {
            k = Integer.parseInt(args[1]);
        }

        if ("receiver".equals(mode)) {
            new ReceiverApp().init(k);
        } else if ("sender".equals(mode)) {
            new SenderApp().init(k);
        }
//        SwingUtilities.invokeLater(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                ChatApp chatApp = new ChatApp();
//                chatApp.init();
//            }
//        });

    }
}
