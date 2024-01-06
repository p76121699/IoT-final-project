# IoT-final-project

This assignment mainly wants to implement the architecture of the paper Electric Vehicle Charging Station using Open Charge Point Protocol (OCPP) and oneM2M Platform for Enhanced Functionality.

https://ieeexplore.ieee.org/document/9707311

## Implementation instructions

1. First, you must download this image. It is recommended to use VMware Workstation 16 Player to open this virtual machine.

https://ncku365-my.sharepoint.com/personal/p76101071_ncku_edu_tw/_layouts/15/onedrive.aspx?id=%2Fpersonal%2Fp76101071%5Fncku%5Fedu%5Ftw%2FDocuments%2F111%E5%B9%B4%E7%89%A9%E8%81%AF%E7%B6%B2%E6%A0%B8%E5%BF%83%E7%B6%B2%E8%B7%AF%E6%8A%80%E8%A1%93%2Flab%5Fimage%2FLa6%2Erar&parent=%2Fpersonal%2Fp76101071%5Fncku%5Fedu%5Ftw%2FDocuments%2F111%E5%B9%B4%E7%89%A9%E8%81%AF%E7%B6%B2%E6%A0%B8%E5%BF%83%E7%B6%B2%E8%B7%AF%E6%8A%80%E8%A1%93%2Flab%5Fimage&ga=1

And through the following instructions, create a bundle. Note that you do not need to follow the steps of jupyter notebook.

https://hackmd.io/@IvgLJq9mT66DRMUQEhYwgw/HyNl-hlNp

In the "Add Dependenices (Show non-exported packages check)" step, you need to add additional 'org.eclipse.om2m.commons.exceptions', 'org.eclipse.om2m.commons.obix', 'org.eclipse. om2m.commons.obix.io', also you need to clear the version

And replace the files 'Activator.java', 'RequestSender.java', 'sm_Constants.java', 'sm_Controller.java', 'sm_Router.java', 'sm_Func.java' with the files I provided. The files are in cse&ipe中

After compilation, according to the instructions, open in and mn in the terminal. After opening, enter 'ss' in mn, and open 'org.eclipse.om2m.ipe.sample_1.1.0', start 41 (assuming org.eclipse.om2m. The number of ipe.sample_1.1.0 is 41)

2. Find postman in the folder and open it, then import the 'miniprojects.postman_collection' I provided. You can see there are many post instructions on the left. Please send these instructions from top to bottom.

3. Enter 'node-red' in the terminal, then open the web page and enter 'http://localhost:1880', open node-red and import the 'node-red_flows' I provided.

## Start experimenting

1. In the previous part, we have started in-cse, mn-cse, postman has also sent requests, and node-red has also been turned on, so in this part we only need to use BlueStacks 5 to open the app I provided ('miniproject_app')

2. In the app, power is initialized. Please enter a value from 0 to 100, ID and password, which must correspond to the container in postman, so if you enter them randomly, the system will show that it is not registered. For IP, please enter the IP of the virtual machine. Finally, enter the amount of power you want to charge, and then you can charge. Please enjoy
