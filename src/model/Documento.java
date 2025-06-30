package model;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Documento implements List<Documento> {
    private int id;
    private String titolo;
    private int idPartecipante;

    public Documento() {
    }

    public Documento(int id, String titolo, int idPartecipante) {
        this.id = id;
        this.titolo = titolo;
        this.idPartecipante = idPartecipante;
    }

    public int getId() {
        return id;
    }

    public String getTitolo() {
        return titolo;
    }

    public int getIdPartecipante() {
        return idPartecipante;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public void setIdPartecipante(int idPartecipante) {
        this.idPartecipante = idPartecipante;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<Documento> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(Documento documento) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Documento> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Documento> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Documento get(int index) {
        return null;
    }

    @Override
    public Documento set(int index, Documento element) {
        return null;
    }

    @Override
    public void add(int index, Documento element) {

    }

    @Override
    public Documento remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<Documento> listIterator() {
        return null;
    }

    @Override
    public ListIterator<Documento> listIterator(int index) {
        return null;
    }

    @Override
    public List<Documento> subList(int fromIndex, int toIndex) {
        return List.of();
    }
}
