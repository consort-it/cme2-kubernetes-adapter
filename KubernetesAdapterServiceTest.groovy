package com.consort.kubernetesadapter.service

import com.consort.kubernetesadapter.restmodel.Namespace
import com.consort.kubernetesadapter.restmodel.Pod
import com.consort.kubernetesadapter.restmodel.Service
import io.fabric8.kubernetes.api.model.DoneableNamespace
import io.fabric8.kubernetes.api.model.DoneableService
import io.fabric8.kubernetes.api.model.NamespaceList
import io.fabric8.kubernetes.api.model.ServiceList
import io.fabric8.kubernetes.client.DefaultKubernetesClient
import io.fabric8.kubernetes.client.dsl.MixedOperation
import io.fabric8.kubernetes.client.dsl.NonNamespaceOperation
import io.fabric8.kubernetes.client.dsl.Resource
import io.fabric8.kubernetes.client.dsl.internal.NamespaceOperationsImpl
import spock.lang.Shared
import spock.lang.Specification

class KubernetesAdapterServiceTest extends Specification {
    String groupString = "dev.k8s.consort-it.de"
    String streamString = "friendly_saha-b76e83fc1ef2"

    @Shared kubernetesReader
    @Shared underTest

/**    def setupSpec() {
        underTest = new KubernetesAPIService()

        kubernetesReader = Stub(DefaultKubernetesClient.class)
        kubernetesReader.namespaces(_) >> { _ ->
            return new NamespaceOperationsImpl(this.httpClient, this.getConfiguration());
        }
        kubernetesReader.namespaces(_) >> { _ ->
            NonNamespaceOperation<io.fabric8.kubernetes.api.model.Namespace, NamespaceList, DoneableNamespace, Resource<io.fabric8.kubernetes.api.model.Namespace, DoneableNamespace>> res = new DescribeLogGroupsResult()
            List<LogGroup> list = new ArrayList<LogGroup>()
            LogGroup group = new LogGroup()
            group.setArn("test")
            group.setCreationTime(12348)
            group.setLogGroupName("dev.k8s.consort-it.de")
            group.setMetricFilterCount(12345)
            group.setRetentionInDays(12)
            group.setStoredBytes(12)
            list.add(group)
            res.setLogGroups(list)
            return res
        }
        kubernetesReader.services(_) >> { _ ->
            MixedOperation<io.fabric8.kubernetes.api.model.Service, ServiceList, DoneableService, Resource<io.fabric8.kubernetes.api.model.Service, DoneableService>> res = new this()
            List<LogStream> list = new ArrayList<LogStream>()
            LogStream stream = new LogStream()
            stream.setArn("test")
            stream.setCreationTime(12348)
            stream.setLogStreamName("friendly_saha-b76e83fc1ef2")
            stream.setStoredBytes(12345)
            stream.setCreationTime(12)
            stream.setFirstEventTimestamp(12)
            stream.setLastEventTimestamp(12)
            stream.setUploadSequenceToken("12")
            list.add(stream)
            res.setLogStreams(list)
            return res
        }
        kubernetesReader.pods(_) >> { _ ->
            GetLogEventsResult res = new GetLogEventsResult()
            List<OutputLogEvent> list = new ArrayList<OutputLogEvent>()
            OutputLogEvent event = new OutputLogEvent()
            event.setMessage("test")
            event.setTimestamp(12348)
            event.setIngestionTime(12345)
            list.add(event)
            res.setEvents(list)
            return res
        }
        kubernetesReader.inNamespace(_) >> { _ ->
            GetLogEventsResult res = new GetLogEventsResult()
            List<OutputLogEvent> list = new ArrayList<OutputLogEvent>()
            OutputLogEvent event = new OutputLogEvent()
            event.setMessage("test")
            event.setTimestamp(12348)
            event.setIngestionTime(12345)
            list.add(event)
            res.setEvents(list)
            return res
        }
        kubernetesReader.list(_) >> { _ ->
            GetLogEventsResult res = new GetLogEventsResult()
            List<OutputLogEvent> list = new ArrayList<OutputLogEvent>()
            OutputLogEvent event = new OutputLogEvent()
            event.setMessage("test")
            event.setTimestamp(12348)
            event.setIngestionTime(12345)
            list.add(event)
            res.setEvents(list)
            return res
        }
        underTest.withKubernetesReader(kubernetesReader)
    }

    def "Check search for namespaces | assuming there are namespaces available"() {
        when:
        List<Namespace> namespaces = underTest.getNamespaces()

        then:
        namespaces.size() > 0
        namespaces[0].getId().contains("e")
    }

    def "Check search for services | assuming there are services available"() {
        when:
        List<Service> services = underTest.getServices("default")

        then:
        services.size() > 0
        services[0].getName().contains("a")
    }

    def "Check to get a service | assuming there is a service available"() {
        when:
        Service service = underTest.getService("default", "jira-adapter-service")

        then:
        service.getName().contains("a")
    }

    def "Check to get multiple pods | assuming there are pods assigned to service available"() {
        when:
        Service service = underTest.getService("default", "jira-adapter-service")
        List<Pod> pods = underTest.getPodsByService("default", service)

        then:
        pods.size() > 0;
        pods.get(0).getName().contains("a")
    }**/
}
