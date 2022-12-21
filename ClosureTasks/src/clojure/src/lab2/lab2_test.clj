(ns lab2-test
  (:require [clojure.test :refer :all]
    (:use lab2)))

(deftest test-prime
  (is (= (nth primes 0) 2))
  (is (= (nth primes 1) 3))
  (is (= (nth primes 2) 5))
  (is (= (nth primes 3) 7))
  (is (= (nth primes 4) 11))
  (is (= (nth primes 5) 13))
  (is (= (nth primes 6) 17))
  (is (= (nth primes 10) 31))
  (is (= (nth primes 100) 547))
  (is (= (nth primes 1000) 7927))
  (is (= (nth primes 2000) 17393))
  (is (= (nth primes 3000) 27457))
  (is (= (nth primes 5000) 48619)))

(test-prime)