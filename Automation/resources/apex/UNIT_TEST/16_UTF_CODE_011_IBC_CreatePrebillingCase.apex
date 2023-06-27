GHI_UTF_IBCOrderProcessing.createOrder();
GHI_UTF_IBCOrderProcessing.createOrderRole('Ordering');
GHI_UTF_IBCOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_IBCOrderProcessing.processOrder($orderId); 
List<Case> prebilling = [Select Id from Case where Type = 'Pre-Billing' and OSM_Primary_Order__c= :$orderId];
System.assert(prebilling.size()>0);