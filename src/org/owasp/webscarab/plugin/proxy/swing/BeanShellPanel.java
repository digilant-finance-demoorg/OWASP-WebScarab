/***********************************************************************
 *
 * $CVSHeader$
 *
 * This file is part of WebScarab, an Open Web Application Security
 * Project utility. For details, please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 2004 Rogan Dawes
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * Getting Source
 * ==============
 *
 * Source for this application is maintained at Sourceforge.net, a
 * repository for free software projects.
 *
 * For details, please see http://www.sourceforge.net/projects/owasp
 *
 */

/*
 * $Id: BeanShellPanel.java,v 1.5 2005/05/18 15:23:31 rogan Exp $
 * ProxyUI.java
 *
 * Created on February 17, 2003, 9:05 PM
 */

package org.owasp.webscarab.plugin.proxy.swing;

import bsh.EvalError;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.owasp.webscarab.plugin.proxy.BeanShell;
import org.owasp.webscarab.plugin.proxy.BeanShellUI;

import org.owasp.webscarab.util.swing.DocumentOutputStream;

import java.io.PrintStream;

import java.util.logging.Logger;

import javax.swing.JPanel;

/**
 *
 * @author  rdawes
 */
public class BeanShellPanel extends javax.swing.JPanel implements ProxyPluginUI,  BeanShellUI {
    
    private BeanShell _beanShell;
    
    private Logger _logger = Logger.getLogger(this.getClass().getName());
    
    private DocumentOutputStream _dos;
    private PrintStream _docStream;
    
    /** Creates new form ManualEditPanel */
    public BeanShellPanel(BeanShell beanShell) {
        _beanShell = beanShell;
        
        initComponents();
        scriptTextArea.registerKeyboardAction(new AutoIndentAction(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);
        configure();
        
        _beanShell.setUI(this);
    }
    
    public String getPluginName() {
        return new String("Bean Shell");
    }
    
    public void configure() {
        boolean enabled = _beanShell.getEnabled();
        enableCheckBox.setSelected(enabled);
        
        scriptTextArea.setEnabled(enabled);
        scriptTextArea.setText(_beanShell.getScript());
        
        commitButton.setEnabled(enabled);
        
        _dos = new DocumentOutputStream(10240);
        _docStream = new PrintStream(_dos);
        logTextArea.setDocument(_dos.getDocument());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jSplitPane1 = new javax.swing.JSplitPane();
        scriptPanel = new javax.swing.JPanel();
        enableCheckBox = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        scriptTextArea = new javax.swing.JTextArea();
        commitButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        logTextArea = new javax.swing.JTextArea();

        setLayout(new java.awt.BorderLayout());

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.8);
        scriptPanel.setLayout(new java.awt.GridBagLayout());

        enableCheckBox.setText("Enabled : ");
        enableCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        enableCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableCheckBoxActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        scriptPanel.add(enableCheckBox, gridBagConstraints);

        scriptTextArea.setFont(new java.awt.Font("Monospaced", 0, 14));
        scriptTextArea.setMargin(new java.awt.Insets(5, 5, 5, 5));
        jScrollPane1.setViewportView(scriptTextArea);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        scriptPanel.add(jScrollPane1, gridBagConstraints);

        commitButton.setText("Commit");
        commitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commitButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        scriptPanel.add(commitButton, gridBagConstraints);

        jSplitPane1.setLeftComponent(scriptPanel);

        jPanel1.setLayout(new java.awt.BorderLayout());

        logTextArea.setBackground(new java.awt.Color(204, 204, 204));
        logTextArea.setEditable(false);
        jScrollPane2.setViewportView(logTextArea);

        jPanel1.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(jPanel1);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents
    
    private void commitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_commitButtonActionPerformed
        try {
            _beanShell.setScript(scriptTextArea.getText());
        } catch (EvalError ee) {
            _logger.severe("Script error: " + ee);
        }
    }//GEN-LAST:event_commitButtonActionPerformed
    
    private void enableCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableCheckBoxActionPerformed
        boolean enabled = enableCheckBox.isSelected();
        _beanShell.setEnabled(enabled);
        scriptTextArea.setEnabled(enabled);
        commitButton.setEnabled(enabled);
    }//GEN-LAST:event_enableCheckBoxActionPerformed
    
    public JPanel getPanel() {
        return this;
    }
    
    public PrintStream getErr() {
        return _docStream;
    }
    
    public PrintStream getOut() {
        return _docStream;
    }
    
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        commitButton.setEnabled(enabled);
        enableCheckBox.setEnabled(enabled);
        scriptTextArea.setEnabled(enabled && enableCheckBox.isSelected());
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton commitButton;
    private javax.swing.JCheckBox enableCheckBox;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextArea logTextArea;
    private javax.swing.JPanel scriptPanel;
    private javax.swing.JTextArea scriptTextArea;
    // End of variables declaration//GEN-END:variables
    
    private static class AutoIndentAction extends AbstractAction {
        public void actionPerformed(ActionEvent ae) {
            JTextArea comp = (JTextArea)ae.getSource();
            Document doc = comp.getDocument();
            
            if(!comp.isEditable())
                return;
            try {
                int line = comp.getLineOfOffset(comp.getCaretPosition());
                
                int start = comp.getLineStartOffset(line);
                int end = comp.getLineEndOffset(line);
                String str = doc.getText(start, end - start - 1);
                String whiteSpace = getLeadingWhiteSpace(str);
                doc.insertString(comp.getCaretPosition(), '\n' + whiteSpace, null);
            } catch(BadLocationException ex) {
                try {
                    doc.insertString(comp.getCaretPosition(), "\n", null);
                } catch(BadLocationException ignore) {
                    // ignore
                }
            }
        }
        
        /**
         *  Returns leading white space characters in the specified string.
         */
        private String getLeadingWhiteSpace(String str) {
            return str.substring(0, getLeadingWhiteSpaceWidth(str));
        }
        
        /**
         *  Returns the number of leading white space characters in the specified string.
         */
        private int getLeadingWhiteSpaceWidth(String str) {
            int whitespace = 0;
            while(whitespace<str.length()) {
                char ch = str.charAt(whitespace);
                if(ch==' ' || ch=='\t')
                    whitespace++;
                else
                    break;
            }
            return whitespace;
        }
    }
    
}
