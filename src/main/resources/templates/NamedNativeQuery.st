 <named-native-query name="$it.Name$" $if(it.isReturnClass)$result-class="$it.ResultClass$" $endif$ $if(it.isResultSetMapping)$result-set-mapping="$it.ResultSetMapping$" $endif$>
    <query><![CDATA[$it.Query$]]></query>
  </named-native-query>