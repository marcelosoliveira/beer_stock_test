package one.digitalinnovation.beerstock.beer.service.interfaces;

import one.digitalinnovation.beerstock.beer.model.Beer;
import one.digitalinnovation.beerstock.exception.BeerAlreadyRegisteredException;
import one.digitalinnovation.beerstock.exception.BeerNotFoundException;

public interface BeerInterface {

    void verifyIfIsAlreadyRegistered(String name) throws BeerAlreadyRegisteredException;

    Beer verifyIfExists(Long id) throws BeerNotFoundException;
}
