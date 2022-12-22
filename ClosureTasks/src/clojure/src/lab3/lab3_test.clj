(ns lab3-test
  (:require [clojure.test :refer :all])
  (:use lab3))


(deftest test-filter-parallel
         (is (= (parallel-filter even? (range 100)) (filter even? (range 100))))
          (is (= (parallel-filter even? (range 1000)) (filter even? (range 1000))))
          (is (= (parallel-filter even? (range 10000)) (filter even? (range 10000))))
          (is (= (parallel-filter even? (range 100000)) (filter even? (range 100000))))
         )

(test-filter-parallel)


(deftest test-filter-parallel-time
  (time (doall (take 100 (filter even? (iterate inc 0)))))
  (time (doall (take 100 (parallel-filter even? (iterate inc 0)))))
  )

(test-filter-parallel-time)