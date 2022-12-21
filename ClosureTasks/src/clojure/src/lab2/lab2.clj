(ns lab2)

; the following function is
; returns a function that takes a number and returns true if the number is a multiple of n
; and false otherwise
(defn multiples? [p]
  (fn  [x] (= 0 (mod x p))))

; the following function is
; return the set of prime numbers
(defn sieve [s]
  (let [p (first s)]
    (cons p (lazy-seq (sieve (filter (complement (multiples? p)) (rest s)))))))
(def primes (sieve (iterate inc 2)))

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
  (println "All tests passed"))

(prime-test)