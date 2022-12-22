(ns lab3)

; split the above function into two functions
; the first function takes a collection and returns a lazy sequence of collections of size n
; the second function takes a collection of collections and returns a lazy sequence of the elements that satisfy the predicate



(defn chunks [n coll]
  ; split coll into chunks of size n
  (let [step (fn step [vs]
               ; if the collection is empty return nil
               ; otherwise return a lazy sequence of the first n elements and the rest of the collection
               (if-let [s (seq vs)]
                 (lazy-seq (cons (take n s) (step (drop n s))))
                 (lazy-seq nil)))]
    (step coll)))

(defn parallel-filter [pred coll]
  ; get the number of processors
  (let [n (. (Runtime/getRuntime) availableProcessors)
        ; split coll into chunks of size n
        ; apply the predicate to each element of each chunk
        futures (map (fn [c] (future (filter pred c))) (chunks n coll))
        ; wait for the futures to complete
        ; in fact just trigger the evaluation of the futures
        ; cause the futures are lazy, and if you ne pnesh ih, to rabotat oni ne budut
        results (map deref futures)]
    ; flatten the result
    (apply concat results)))
