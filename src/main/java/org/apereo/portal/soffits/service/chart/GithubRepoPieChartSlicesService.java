package org.apereo.portal.soffits.service.chart;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apereo.portal.soffits.service.PieChartSlice;
import org.apereo.portal.soffits.service.PieChartSlicesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service("githubRepoPieChartSlicesService")
public class GithubRepoPieChartSlicesService implements PieChartSlicesService {

    private static final String PUBLIC_REPOS_FIELD = "public_repos";

    private static final int TIMEOUT_SECONDS = 10;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();

    private final RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(TIMEOUT_SECONDS * 1000)
            .setConnectTimeout(TIMEOUT_SECONDS * 1000)
            .build();

    private final CloseableHttpClient httpClient = HttpClientBuilder
            .create()
            .setDefaultRequestConfig(requestConfig)
            .setConnectionManager(poolingHttpClientConnectionManager)
            .build();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${github.api.prefix:https://api.github.com/orgs/}")
    private String githubApiPrefix;

    @Cacheable("pieChartSlicesCache")
    @Override
    public PieChartSlice getSlice(String name) {

        PieChartSlice rslt = null;  // default

        final String url = githubApiPrefix + name;

        logger.debug("Accessing GitHub info for organization at URL '{}'", url);

        final HttpGet getMethod = new HttpGet(url);
        HttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(getMethod);

            final int statusCode = httpResponse.getStatusLine().getStatusCode();
            logger.debug("HTTP response code for url '{}' was '{}'", url, statusCode);

            if (statusCode != HttpStatus.SC_OK) {
                logger.error("Failed to access GitHub info at URL '{}';  HttpStatus={}", url, statusCode);
                return null;
            }

            final HttpEntity entity = httpResponse.getEntity();
            final JsonNode node = objectMapper.readValue(entity.getContent(), JsonNode.class);

            logger.debug("JSON returned from GitHub API.../n{}", node.toString());

            final JsonNode repoCount = node.get(PUBLIC_REPOS_FIELD);

            rslt = new PieChartSlice(name, BigDecimal.valueOf(repoCount.intValue()));

        } catch (IOException e) {
            logger.error("IOException for URL '{}'", url);
        }

        return rslt;

    }

}
