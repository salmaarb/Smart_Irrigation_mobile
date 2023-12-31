package ma.projet.arrosageintellegentv2;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import ma.projet.arrosageintellegentv2.beans.Grandeur;

public class GraphFragment extends Fragment {

    private GraphView graph;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_graph, container, false);

        graph = root.findViewById(R.id.graph);

        Bundle bundle = getArguments();
        if (bundle != null) {
            List<Grandeur> grandeurs = bundle.getParcelableArrayList("grandeurs");

            if (grandeurs != null) {
                updateGraph(graph, grandeurs);
            }
        }

        return root;
    }

    private void updateGraph(GraphView graph, List<Grandeur> grandeurs) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

        for (int i = 0; i < grandeurs.size(); i++) {
            Grandeur grandeur = grandeurs.get(i);
            // Utilisez l'indice ou le temps comme X, et la valeur de grandeur comme Y
            series.appendData(new DataPoint(i, grandeur.getHumidity()), true, grandeurs.size());
        }

        graph.removeAllSeries();
        graph.addSeries(series);

        customizeGraph(graph);
    }

    private void customizeGraph(GraphView graph) {
        // Personnalisez l'apparence des graphiques si nécessaire
        // ...
        // Par exemple, personnalisez les labels de l'axe X pour afficher l'heure au format HH:mm:ss
        graph.setTitle("Graph Grandeurs");
    }
}
