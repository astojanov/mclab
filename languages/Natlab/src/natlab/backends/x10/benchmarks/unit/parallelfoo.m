function [x] = parallelfoo(a)                                                    
    %!finish                                                                 
         for (i = 1:length(a))                                                    
                 %!async                                                          
                    a(i)=a(i)*2;                                                     
                 %!end                                                            
         end                                                                      
    %!end           
         x=a;                                                         
end              

