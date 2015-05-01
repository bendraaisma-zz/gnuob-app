package com.netbrasoft.gnuob.application.category;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.wicket.MetaDataKey;
import org.apache.wicket.Session;

import com.netbrasoft.gnuob.api.SubCategory;

public class SubCategoryExpansion implements Set<SubCategory>, Serializable {

   private static final long serialVersionUID = -115456451423752276L;

   private static MetaDataKey<SubCategoryExpansion> KEY = new MetaDataKey<SubCategoryExpansion>() {

      private static final long serialVersionUID = 689335047835010940L;
   };

   public static SubCategoryExpansion get() {

      SubCategoryExpansion expansion = Session.get().getMetaData(KEY);
      if (expansion == null) {
         expansion = new SubCategoryExpansion();

         Session.get().setMetaData(KEY, expansion);
      }
      return expansion;
   }

   private transient Set<Long> ids = new HashSet<Long>();

   private boolean inverse;

   @Override
   public boolean add(SubCategory subCategory) {
      if (inverse) {
         return ids.remove(subCategory.getId());
      } else {
         return ids.add(subCategory.getId());
      }
   }

   @Override
   public boolean addAll(Collection<? extends SubCategory> c) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void clear() {
      throw new UnsupportedOperationException();
   }

   public void collapseAll() {
      ids.clear();

      inverse = false;
   }

   @Override
   public boolean contains(Object object) {
      SubCategory subCategory = (SubCategory) object;

      if (inverse) {
         return !ids.contains(subCategory.getId());
      } else {
         return ids.contains(subCategory.getId());
      }
   }

   @Override
   public boolean containsAll(Collection<?> c) {
      throw new UnsupportedOperationException();
   }

   public void expandAll() {
      ids.clear();

      inverse = true;
   }

   @Override
   public boolean isEmpty() {
      throw new UnsupportedOperationException();
   }

   @Override
   public Iterator<SubCategory> iterator() {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean remove(Object object) {
      SubCategory subCategory = (SubCategory) object;

      if (inverse) {
         return ids.add(subCategory.getId());
      } else {
         return ids.remove(subCategory.getId());
      }
   }

   @Override
   public boolean removeAll(Collection<?> c) {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean retainAll(Collection<?> c) {
      throw new UnsupportedOperationException();
   }

   @Override
   public int size() {
      throw new UnsupportedOperationException();
   }

   @Override
   public Object[] toArray() {
      throw new UnsupportedOperationException();
   }

   @Override
   public <T> T[] toArray(T[] a) {
      throw new UnsupportedOperationException();
   }
}
