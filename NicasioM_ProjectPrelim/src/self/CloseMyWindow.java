/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self;

import java.awt.event.*;
public class CloseMyWindow extends WindowAdapter {

    @Override
    public void windowClosing (WindowEvent e)
    {
        System.exit(0);
    }
    
}
