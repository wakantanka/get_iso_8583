package com.wirecard.acqp.two.jmeter.samplers;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
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
	public static final String WIKIPAGE = "DummySampler";
    private JCheckBox isSuccessful;
    private JTextField responseCode;
    private JTextField responseMessage;
    private JTextField responseTime;
    private JTextArea responseData;
    private JTextArea requestData;
    private JCheckBox isWaiting;
    private JTextField latency;

    /**
     *
     */
    public InterchangeAccessorySamplerGui() {
        init();
    }


    @Override
    public void configure(TestElement element) {
        super.configure(element);

        isSuccessful.setSelected(element.getPropertyAsBoolean(InterchangeAccessorySampler.IS_SUCCESSFUL));
        isWaiting.setSelected(element.getPropertyAsBoolean(InterchangeAccessorySampler.IS_WAITING));
        responseCode.setText(element.getPropertyAsString(InterchangeAccessorySampler.RESPONSE_CODE));
        responseMessage.setText(element.getPropertyAsString(InterchangeAccessorySampler.RESPONSE_MESSAGE));
        requestData.setText(element.getPropertyAsString(InterchangeAccessorySampler.REQUEST_DATA));
        responseData.setText(element.getPropertyAsString(InterchangeAccessorySampler.RESPONSE_DATA));
        responseTime.setText(element.getPropertyAsString(InterchangeAccessorySampler.RESPONSE_TIME));
        latency.setText(element.getPropertyAsString(InterchangeAccessorySampler.LATENCY));
    }

    public TestElement createTestElement() {
        InterchangeAccessorySampler sampler = new InterchangeAccessorySampler();
        modifyTestElement(sampler);
//        sampler.setComment(JMeterPluginsUtils.getWikiLinkText(WIKIPAGE));
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
            InterchangeAccessorySampler dummySampler = (InterchangeAccessorySampler) sampler;
            dummySampler.setSimulateWaiting(isWaiting.isSelected());
            dummySampler.setSuccessful(isSuccessful.isSelected());
            dummySampler.setResponseCode(responseCode.getText());
            dummySampler.setResponseMessage(responseMessage.getText());
            dummySampler.setRequestData(requestData.getText());
            dummySampler.setResponseData(responseData.getText());
            dummySampler.setResponseTime(responseTime.getText());
            dummySampler.setLatency(latency.getText());
        }
    }

    @Override
    public void clearGui() {
        super.clearGui();
        initFields();
    }

    private void initFields() {
        isSuccessful.setSelected(true);
        isWaiting.setSelected(true);
        responseCode.setText("200");
        responseMessage.setText("OK");
        requestData.setText("Dummy Sampler used to simulate requests and responses\nwithout actual network activity. This helps debugging tests.");
        responseData.setText("Dummy Sampler used to simulate requests and responses\nwithout actual network activity. This helps debugging tests.");
        responseTime.setText("${__Random(100,1000)}");
        latency.setText("${__Random(1,100)}");
    }

    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    private void init() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

//        add(JMeterPluginsUtils.addHelpLinkToPanel(makeTitlePanel(), WIKIPAGE), BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.anchor = GridBagConstraints.FIRST_LINE_END;

        GridBagConstraints editConstraints = new GridBagConstraints();
        editConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        editConstraints.weightx = 1.0;
        editConstraints.fill = GridBagConstraints.HORIZONTAL;

        addToPanel(mainPanel, labelConstraints, 0, 0, new JLabel("Successfull sample: ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 0, isSuccessful = new JCheckBox());
        addToPanel(mainPanel, labelConstraints, 0, 1, new JLabel("Response Code (eg 200): ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 1, responseCode = new JTextField(20));

        editConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(2, 0, 0, 0);

        addToPanel(mainPanel, labelConstraints, 0, 2, new JLabel("Response Message (eg OK): ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 2, responseMessage = new JTextField(20));
        addToPanel(mainPanel, labelConstraints, 0, 3, new JLabel("Latency (milliseconds): ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 3, latency = new JTextField(20));
        addToPanel(mainPanel, labelConstraints, 0, 4, new JLabel("Response Time (milliseconds): ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 4, responseTime = new JTextField(20));
        addToPanel(mainPanel, labelConstraints, 0, 5, new JLabel("Simulate Response Time (sleep): ", JLabel.RIGHT));
        addToPanel(mainPanel, editConstraints, 1, 5, isWaiting = new JCheckBox());

        editConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        labelConstraints.insets = new java.awt.Insets(4, 0, 0, 0);

        addToPanel(mainPanel, labelConstraints, 0, 6, new JLabel("Request Data: ", JLabel.RIGHT));
        editConstraints.fill = GridBagConstraints.BOTH;
        requestData = new JTextArea();
//        addToPanel(mainPanel, editConstraints, 1, 6, GuiBuilderHelper.getTextAreaScrollPaneContainer(requestData, 10));

        addToPanel(mainPanel, labelConstraints, 0, 7, new JLabel("Response Data: ", JLabel.RIGHT));
        editConstraints.fill = GridBagConstraints.BOTH;

        responseData = new JTextArea();
//        addToPanel(mainPanel, editConstraints, 1, 7, GuiBuilderHelper.getTextAreaScrollPaneContainer(responseData, 10));

        JPanel container = new JPanel(new BorderLayout());
        container.add(mainPanel, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);
    }

    private void addToPanel(JPanel panel, GridBagConstraints constraints, int col, int row, JComponent component) {
        constraints.gridx = col;
        constraints.gridy = row;
        panel.add(component, constraints);
    }
}
