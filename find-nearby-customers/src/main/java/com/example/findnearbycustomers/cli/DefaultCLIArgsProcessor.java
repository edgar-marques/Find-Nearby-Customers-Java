package com.example.findnearbycustomers.cli;

import com.example.findnearbycustomers.customer.dto.CustomerRecord;
import com.example.findnearbycustomers.customer.model.Customer;
import com.example.findnearbycustomers.customer.service.CustomerService;
import com.example.findnearbycustomers.io.FileParser;
import lombok.extern.log4j.Log4j2;
import ma.glasnost.orika.MapperFacade;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Singleton
public class DefaultCLIArgsProcessor implements CLIArgsProcessor {

    private FileParser fileParser;
    private MapperFacade mapper;
    private CustomerService customerService;

    @Inject
    public DefaultCLIArgsProcessor(FileParser fileParser, MapperFacade mapper, CustomerService customerService) {
        this.fileParser = fileParser;
        this.mapper = mapper;
        this.customerService = customerService;
    }

    @Override
    public void process(@Valid @NotNull CLIArgs args) {
        if (args.isVerbose()) {
            log.info("Verbose mode enabled");
        }

        List<CustomerRecord> customersRecords =
                fileParser.parse(args.getInputFile(), CustomerRecord.class);

        List<Customer> customers = customersRecords.stream()
                .map(r -> mapper.map(r, Customer.class))
                .collect(Collectors.toList());

        List<Customer> customersWithinRange =
                customerService.findCustomersWithinRange(
                        customers,
                        args.getTargetLocation(),
                        args.getRadius().doubleValue() * 1_000.0 /* 1km */);

        customersWithinRange.stream()
                .sorted(Comparator.comparing(Customer::getUserId))
                .forEach(r -> log.info(r.toString()));
    }
}
