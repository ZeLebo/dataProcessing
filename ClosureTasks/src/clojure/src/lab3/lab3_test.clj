(ns lab3-test
  (:require [clojure.test :refer :all])
  (:use lab3))


(deftest test-filter-parallel
         (is (= (pfilter even? (range 100)) (filter even? (range 100))))
          (is (= (pfilter even? (range 1000)) (filter even? (range 1000))))
          (is (= (pfilter even? (range 10000)) (filter even? (range 10000))))
          (is (= (pfilter even? (range 100000)) (filter even? (range 100000))))
         )

(test-filter-parallel)