GHI_UTF_ColonOrderProcessing.createOrder();
GHI_UTF_ColonOrderProcessing.createOrderRole('Ordering');
GHI_UTF_ColonOrderProcessing.createOrderRole('Specimen Submitting');
GHI_UTF_ColonOrderProcessing.processOrder($orderId);
GHI_UTF_ColonOrderProcessing.closePreBillingCase($orderId);
GHI_UTF_ColonOrderProcessing.createPackage($orderId);
GHI_UTF_ColonOrderProcessing.closeSpecArrivalCase($orderId);
GHI_UTF_ColonOrderProcessing.createSpecimen($orderId);
GHI_UTF_ColonOrderProcessing.createResult($orderId);
GHI_UTF_ColonOrderProcessing.updateOLIToDVC($orderId);
Order ord = [Select OrderNumber from Order where id= :$orderId];
List<ICRT_Message_Log__c> renanddist = [Select Id from ICRT_Message_Log__c where ICRT_Message_Type__c = 'RenderAndDistributeDataMessage' and ICRT_Object_ID__c= :ord.OrderNumber ];
System.assert(renanddist.size()>0);