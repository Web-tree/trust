package org.webtree.trust.service.social.api;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.webtree.social.stackexchange.api.StackExchange;
import org.webtree.social.stackexchange.domain.NetworkUser;
import org.webtree.social.stackexchange.domain.Site;
import org.webtree.social.stackexchange.domain.User;
import org.webtree.trust.domain.StackExchangeUser;
import org.webtree.trust.service.social.StackExchangeTemplateFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class StackExchangeApi implements SocialApi<StackExchangeUser> {
    private StackExchangeTemplateFactory factory;
    private ModelMapper modelMapper;

    public StackExchangeApi(StackExchangeTemplateFactory facebookFactory, ModelMapper modelMapper) {
        this.factory = facebookFactory;
        this.modelMapper = modelMapper;
    }

    public StackExchangeUser getUser(String token) {
        StackExchange stackExchangeService = factory.create(token);
        List<Site> sites = stackExchangeService.siteOperations().getActualSites();
        List<NetworkUser> users = stackExchangeService.networkUserOperations().getUserAssociatedAccounts();
        Optional<NetworkUser> networkUser =
                users
                        .stream()
                        .max(Comparator.comparing(NetworkUser::getReputation));

        Optional<Site> site =
                sites
                        .stream()
                        .filter((x) -> x.getSiteUrl().equals((networkUser.get())
                                .getSiteUrl())).findFirst();

        Optional<User> user = stackExchangeService
                .userOperations()
                .getUserProfileBySiteName((site.get())
                        .getApiSiteParameter());

        return modelMapper.map(user.get(), StackExchangeUser.class);
    }
}
