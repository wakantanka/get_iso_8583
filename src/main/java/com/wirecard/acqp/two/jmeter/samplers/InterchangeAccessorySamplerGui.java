package com.wirecard.acqp.two.jmeter.samplers;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;

/**
 *
 * @author Wirecard AG (c) 2014. All rights reserved.
 */
public class InterchangeAccessorySamplerGui
        extends AbstractSamplerGui {

    /**
	 * 
	 */
	private static final long serialVersionUID = 9216502088765956186L;
	private JTextField twoInput;
//    private JCheckBox isSuccessful;
    private JTextField cardSchema;
    private JTextField fieldPath;

    /**
     *
     */
    public InterchangeAccessorySamplerGui() {
        init();
    }


    @Override
    public void configure(TestElement element) {
        super.configure(element);

        cardSchema.setText(element.getPropertyAsString(InterchangeAccessorySampler.CARD_SCHEMA));
        fieldPath.setText(element.getPropertyAsString(InterchangeAccessorySampler.FIELD_PATH));
        twoInput.setText(element.getPropertyAsString(InterchangeAccessorySampler.TWO_INPUT));
    }

    public TestElement createTestElement() {
        InterchangeAccessorySampler sampler = new InterchangeAccessorySampler();
        modifyTestElement(sampler);
        return sampler;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     *
     * @param sampler
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    public void modifyTestElement(TestElement sampler) {
        super.configureTestElement(sampler);

        if (sampler instanceof InterchangeAccessorySampler) {
            InterchangeAccessorySampler interchangeAccessorySampler = (InterchangeAccessorySampler) sampler;
            interchangeAccessorySampler.setCardSchema(cardSchema.getText());
            interchangeAccessorySampler.setFieldPath(fieldPath.getText());
            interchangeAccessorySampler.setTwoInput(twoInput.getText());
        }
    }

    @Override
    public void clearGui() {
        super.clearGui();
        initFields();
    }

    private void initFields() {
    	twoInput.setText("the HexString of an ISO8583 InterchangeMsg");
        cardSchema.setText("VISA");
        fieldPath.setText("2");
    }

    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    private void init() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());


        JPanel mainPanel = new JPanel(new GridBagLayout());

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.anchor = GridBagConstraints.FIRST_LINE_END;

        GridBagConstraints editConstraints = new GridBagConstraints();
        editConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        editConstraints.weightx = 1.0;
        editConstraints.fill = GridBagConstraints.HORIZONTAL;

        addToPanel(mainPanel, labelConstraints, 0, 0, new JLabel("Two Input ISO8583 InterchangeMsg: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 0,  twoInput = new JTextField(255));

                
        addToPanel(mainPanel, labelConstraints, 0, 1, new JLabel("CardScheme (VISA, MASTERCARD, JCB): ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 1, cardSchema = new JTextField(20));

        editConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(2, 0, 0, 0);

        addToPanel(mainPanel, labelConstraints, 0, 2, new JLabel("FieldPath (e. g. 62.2): ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 2, fieldPath = new JTextField(20));


        editConstraints.fill = GridBagConstraints.BOTH;

        JPanel container = new JPanel(new BorderLayout());
        container.add(mainPanel, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);
        container.setVisible(true);
        mainPanel.setVisible(true);
        
    }

    private void addToPanel(JPanel panel, GridBagConstraints constraints, int col, int row, JComponent component) {
        constraints.gridx = col;
        constraints.gridy = row;
        panel.add(component, constraints);
    }
}
