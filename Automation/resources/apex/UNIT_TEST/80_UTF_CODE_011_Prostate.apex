GHI_UTF_ProstateOrderProcessing.createOrder();
GHI_UTF_ProstateOrderProcessing.createOrderRole('Ordering');
GHI_UTF_ProstateOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_ProstateOrderProcessing.processOrder($orderId); 
List<Case> prebilling = [Select Id from Case where Type = 'Pre-Billing' and OSM_Primary_Order__c= :$orderId];
System.assert(prebilling.size()>0);