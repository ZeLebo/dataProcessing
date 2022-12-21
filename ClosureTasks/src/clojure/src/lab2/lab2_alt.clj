(ns lab2_alt)

(def primes
  (lazy-seq (filter
              ; filter out all numbers that are not prime
              (fn [i] (not-any?
                        ; check if any of the numbers from 2 to i-1 is a divisor of i
                        (fn [p] (= 0 (mod i p)))
                        ; the numbers from 2 to i-1
                        (range 2 (inc (int (Math/sqrt i))))))
              ; the numbers from 2 to infinity
                        (iterate inc 2))))

(defn prime-test []
  (assert (= (nth primes 0) 2))
  (assert (= (nth primes 1) 3))
  (assert (= (nth primes 2) 5))
  (assert (= (nth primes 3) 7))
  (assert (= (nth primes 4) 11))
  (assert (= (nth primes 5) 13))
  (assert (= (nth primes 6) 17))
  (assert (= (nth primes 10) 31))
  (assert (= (nth primes 100) 547))
  (assert (= (nth primes 1000) 7927))
  (assert (= (nth primes 2000) 17393))
  (assert (= (nth primes 3000) 27457))
  (assert (= (nth primes 5000) 48619))
  (assert (= (nth primes 9999) 104729))
  (assert (= (nth primes 10000) 104743))
  (println "All tests passed"))

(prime-test)

; same as above but with a different implementation
(def primes2
  (lazy-seq (filter
              (fn [i] (not-any?
                        (fn [p] (zero? (rem i p)))
                        (take-while
                          (fn [n] (<= (* n n) i))
                          primes2)))
              (drop 2 (range)))))
