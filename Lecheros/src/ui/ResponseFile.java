package ui;


import java.awt.Toolkit;
import javax.swing.SwingWorker;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Edu
 */

public class ResponseFile extends SwingWorker<String, Void>{
        @Override
        protected String doInBackground() throws Exception {
                for (int i = 0; i <= 20; i++) {
                    
                        setProgress((i+1) * 100 / 20);
                        Thread.sleep(15);
                }
                return "";
        }
       
    @Override
    protected void done() {
        setProgress(100);
        Toolkit.getDefaultToolkit().beep();
    }
}

