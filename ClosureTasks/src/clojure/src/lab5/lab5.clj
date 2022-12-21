(ns lab5)
(defn philosopher
  "A philosopher that eats and thinks
   id - a philosopher id
   left-fork - a ref to a left fork
   right-fork - a ref to a right fork
   think-time - a time to think in milliseconds
   eat-time - a time to eat in milliseconds
   returns a thread that must be run explicitly"
  [id left-fork right-fork think-time eat-time]
  (new Thread
       (fn []
         (loop []
           (Thread/sleep think-time)
           (println (str "Philosopher " id " is thinking\n"))
           (dosync
            (alter left-fork :counter inc)
            (alter right-fork :counter inc))
           (Thread/sleep eat-time)
           (println (str "Philosopher " id " is eating\n"))
           (dosync
            (alter left-fork :counter dec)
            (alter right-fork :counter dec))
           (Thread/sleep think-time)
           (recur)))))

(defn create-fork
  "Creates a fork
   returns a ref to a fork"
  []
  (ref {:counter 0}))

(defn create-philosophers
  "Creates a list of philosophers
   n - a number of philosophers
   forks - a list of forks
   think-time - a time to think in milliseconds
   eat-time - a time to eat in milliseconds
   returns a list of threads that must be run explicitly"
  [n forks think-time eat-time]
  (let [philosophers (map (fn [i] (philosopher i (nth forks i) (nth forks (mod (+ i 1) n)) think-time eat-time)) (range n))]
    philosophers))

(defn run-philosophers
  "Runs a list of philosophers
   philosophers - a list of threads"
  [philosophers]
  (doseq [philosopher philosophers]
    (.start philosopher)))

; main function

(defn main
  "Main function"
  []
  (let [n 5
        forks (map (fn [i] (create-fork)) (range n))
        think-time 1000
        eat-time 1000
        philosophers (create-philosophers n forks think-time eat-time)]
    (run-philosophers philosophers)))

; run main function

(main)