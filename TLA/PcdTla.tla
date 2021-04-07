------------------------------- MODULE PcdTla -------------------------------
EXTENDS TLC, Integers, Sequences

(*--algorithm message_queue
variable queuePdf = <<"pdf1","pdf2","pdf3","pdf4","pdf5","pdf6">>


process worker \in { "worker1","worker2"}
variable item = "none";
begin Compute:
  while Len(queuePdf) > 0 do
    take: 
        if(Len(queuePdf) > 1)
        then item := Head(queuePdf);
        queuePdf := Tail(queuePdf);
        else if(Len(queuePdf) > 0)
        then item := Head(queuePdf);
        queuePdf := <<>>;
        end if;
        end if;
    compute:
        print item;
  end while;
end process;
end algorithm;*)

\* BEGIN TRANSLATION (chksum(pcal) = "7970fbcc" /\ chksum(tla) = "13ebd879")
VARIABLES queuePdf, pc, item

vars == << queuePdf, pc, item >>

ProcSet == ({ "worker1","worker2"})

Init == (* Global variables *)
        /\ queuePdf = <<"pdf1","pdf2","pdf3","pdf4","pdf5","pdf6">>
        (* Process worker *)
        /\ item = [self \in { "worker1","worker2"} |-> "none"]
        /\ pc = [self \in ProcSet |-> "Compute"]

Compute(self) == /\ pc[self] = "Compute"
                 /\ IF Len(queuePdf) > 0
                       THEN /\ pc' = [pc EXCEPT ![self] = "take"]
                       ELSE /\ pc' = [pc EXCEPT ![self] = "Done"]
                 /\ UNCHANGED << queuePdf, item >>

take(self) == /\ pc[self] = "take"
              /\ IF (Len(queuePdf) > 1)
                    THEN /\ item' = [item EXCEPT ![self] = Head(queuePdf)]
                         /\ queuePdf' = Tail(queuePdf)
                    ELSE /\ IF (Len(queuePdf) > 0)
                               THEN /\ item' = [item EXCEPT ![self] = Head(queuePdf)]
                                    /\ queuePdf' = <<>>
                               ELSE /\ TRUE
                                    /\ UNCHANGED << queuePdf, item >>
              /\ pc' = [pc EXCEPT ![self] = "compute"]

compute(self) == /\ pc[self] = "compute"
                 /\ PrintT(item[self])
                 /\ pc' = [pc EXCEPT ![self] = "Compute"]
                 /\ UNCHANGED << queuePdf, item >>

worker(self) == Compute(self) \/ take(self) \/ compute(self)

(* Allow infinite stuttering to prevent deadlock on termination. *)
Terminating == /\ \A self \in ProcSet: pc[self] = "Done"
               /\ UNCHANGED vars

Next == (\E self \in { "worker1","worker2"}: worker(self))
           \/ Terminating

Spec == Init /\ [][Next]_vars

Termination == <>(\A self \in ProcSet: pc[self] = "Done")

\* END TRANSLATION 

=============================================================================
\* Modification History
\* Last modified Tue Apr 06 20:15:09 CEST 2021 by hama
\* Created Tue Apr 06 20:14:29 CEST 2021 by hama
