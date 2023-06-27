GHI_UTF_DCISOrderProcessing.createOrder();
GHI_UTF_DCISOrderProcessing.createOrderRole('Ordering');
GHI_UTF_DCISOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_DCISOrderProcessing.processOrder($orderId); 
List<Case> prebilling = [Select Id from Case where Type = 'Pre-Billing' and OSM_Primary_Order__c= :$orderId];
System.assert(prebilling.size()>0);