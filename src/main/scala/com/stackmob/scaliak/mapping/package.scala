package com.stackmob.scaliak

import scalaz._
import Scalaz._

/**
 * Created by IntelliJ IDEA.
 * User: jordanrw
 * Date: 12/26/11
 * Time: 11:54 PM
 */

package object mapping {
  
  def riakKey(p: String => Boolean = (s => true))(obj: ReadObject): ValidationNEL[Throwable, String] =
    readValue("key", p, _.key, obj)
  
  
  def stringValue(p: String => Boolean = (s => true))(obj: ReadObject): ValidationNEL[Throwable, String] =
    readValue("string value", p, _.stringValue, obj)
  
  def bytesValue(p: Array[Byte] => Boolean = (s => true))(obj: ReadObject): ValidationNEL[Throwable, Array[Byte]] =
    readValue("value", p, _.bytes, obj)
  
  def links(p: Option[NonEmptyList[ScaliakLink]] => Boolean = (s => true))(obj: ReadObject): ValidationNEL[Throwable, Option[NonEmptyList[ScaliakLink]]] =
    readValue("links (allowing for empty list)", p, _.links, obj)
  
  def nonEmptyLinks(p: NonEmptyList[ScaliakLink] => Boolean = (s => true))(obj: ReadObject): ValidationNEL[Throwable, NonEmptyList[ScaliakLink]] =
    obj.links some { ls =>
      if (p(ls)) ls.successNel[Throwable] else MappingError("links (non-empty)", ls).failureNel
    } none {
      MappingError("links (non-empty)", obj.links).failureNel
    }
  
  def riakMetadata(key: String, p: String => Boolean = (s => true))(obj: ReadObject): ValidationNEL[Throwable, String] =
    obj.getMetadata(key) some { v =>
      if (p(v)) v.successNel[Throwable] else MetadataMappingError(key, v).failureNel
    } none {
      MissingMetadataMappingError(key).failureNel
    }

  private[mapping] def readValue[T](name: String, pf: T => Boolean, vf: ReadObject => T, obj: ReadObject): ValidationNEL[Throwable, T] = {
    val value = vf(obj)
    if (pf(value)) value.successNel else (MappingError(name, value)).failureNel
  }

  implicit def Func1Lift[A, T](f: A => T) = new {
    def fromScaliak(f1: ReadObject => ValidationNEL[Throwable, A]): ReadObject => ValidationNEL[Throwable, T] = {
      (obj: ReadObject) => (f1(obj) map f)
    }
  }

  implicit def Func2Lift[A, B, T](f: (A, B) => T) = new {
    def fromScaliak(f1: ReadObject => ValidationNEL[Throwable, A],
                    f2: ReadObject => ValidationNEL[Throwable, B]): ReadObject => ValidationNEL[Throwable, T] = {
      (obj: ReadObject) => (f1(obj) |@| f2(obj))(f)
    }
  }
  
  implicit def Func3Lift[A, B, C, T](f: (A, B, C) => T) = new {
    def fromScaliak(f1: ReadObject => ValidationNEL[Throwable, A],
                    f2: ReadObject => ValidationNEL[Throwable, B],
                    f3: ReadObject => ValidationNEL[Throwable, C]): ReadObject => ValidationNEL[Throwable, T] = {
      obj => (f1(obj) |@| f2(obj) |@| f3(obj))(f)
    }
  }
  
  implicit def Func4Lift[A, B, C, D, T](f: (A, B, C, D) => T) = new {
    def fromScaliak(f1: ReadObject => ValidationNEL[Throwable, A],
                    f2: ReadObject => ValidationNEL[Throwable, B],
                    f3: ReadObject => ValidationNEL[Throwable, C],
                    f4: ReadObject => ValidationNEL[Throwable, D]): ReadObject => ValidationNEL[Throwable, T] = {
      obj => (f1(obj) |@| f2(obj) |@| f3(obj) |@| f4(obj))(f)
    }
  }
  
  implicit def Func5Lift[A, B, C, D, E, T](f: (A, B, C, D, E) => T) = new {
    def fromScaliak(f1: ReadObject => ValidationNEL[Throwable, A],
                    f2: ReadObject => ValidationNEL[Throwable, B],
                    f3: ReadObject => ValidationNEL[Throwable, C],
                    f4: ReadObject => ValidationNEL[Throwable, D],
                    f5: ReadObject => ValidationNEL[Throwable, E]): ReadObject => ValidationNEL[Throwable, T] = {
      obj => (f1(obj) |@| f2(obj) |@| f3(obj) |@| f4(obj) |@| f5(obj))(f)
    }
  }
  
  implicit def Func6Lift[A, B, C, D, E, F, T](f: (A, B, C, D, E, F) => T) = new {
    def fromScaliak(f1: ReadObject => ValidationNEL[Throwable, A],
                    f2: ReadObject => ValidationNEL[Throwable, B],
                    f3: ReadObject => ValidationNEL[Throwable, C],
                    f4: ReadObject => ValidationNEL[Throwable, D],
                    f5: ReadObject => ValidationNEL[Throwable, E],
                    f6: ReadObject => ValidationNEL[Throwable, F]): ReadObject => ValidationNEL[Throwable, T] = {
      obj => (f1(obj) |@| f2(obj) |@| f3(obj) |@| f4(obj) |@| f5(obj) |@| f6(obj))(f)
    }
  }
  
  implicit def Func7Lift[A, B, C, D, E, F, G, T](f: (A, B, C, D, E, F, G) => T) = new {
    def fromScaliak(f1: ReadObject => ValidationNEL[Throwable, A],
                    f2: ReadObject => ValidationNEL[Throwable, B],
                    f3: ReadObject => ValidationNEL[Throwable, C],
                    f4: ReadObject => ValidationNEL[Throwable, D],
                    f5: ReadObject => ValidationNEL[Throwable, E],
                    f6: ReadObject => ValidationNEL[Throwable, F],
                    f7: ReadObject => ValidationNEL[Throwable, G]): ReadObject => ValidationNEL[Throwable, T] = {
      obj => (f1(obj) |@| f2(obj) |@| f3(obj) |@| f4(obj) |@| f5(obj) |@| f6(obj) |@| f7(obj))(f)
    }
  }
    
}
